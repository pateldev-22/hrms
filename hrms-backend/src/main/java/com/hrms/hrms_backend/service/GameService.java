package com.hrms.hrms_backend.service;
import com.hrms.hrms_backend.constants.GameType;
import com.hrms.hrms_backend.dto.game.*;
import com.hrms.hrms_backend.entity.*;
import com.hrms.hrms_backend.exception.CustomException;
import com.hrms.hrms_backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameService {

    private final GameConfigurationRepository configRepository;
    private final GameSlotRepository slotRepository;
    private final GameBookingRepository bookingRepository;
    private final GameBookingParticipantRepository participantRepository;
    private final UserRepository userRepository;

    public GameService(GameConfigurationRepository configRepository,
                       GameSlotRepository slotRepository,
                       GameBookingRepository bookingRepository,
                       GameBookingParticipantRepository participantRepository,
                       UserRepository userRepository) {
        this.configRepository = configRepository;
        this.slotRepository = slotRepository;
        this.bookingRepository = bookingRepository;
        this.participantRepository = participantRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public GameConfigurationResponse createOrUpdateConfig(GameConfigurationRequest request) {
        GameConfiguration config = configRepository.findByGameType(request.getGameType())
                .orElse(new GameConfiguration());

        config.setGameType(request.getGameType());
        config.setOperatingHoursStart(request.getOperatingHoursStart());
        config.setOperatingHoursEnd(request.getOperatingHoursEnd());
        config.setSlotDurationMinutes(request.getSlotDurationMinutes());
        config.setMaxPlayersPerSlot(request.getMaxPlayersPerSlot());
        config.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);

        GameConfiguration saved = configRepository.save(config);
        return mapToConfigResponse(saved);
    }

    public List<GameConfigurationResponse> getAllConfigs() {
        return configRepository.findAll().stream()
                .map(this::mapToConfigResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void generateSlots(GameType gameType, LocalDate date) {
        GameConfiguration config = configRepository.findByGameType(gameType)
                .orElseThrow(() -> new CustomException("Game configuration not found"));

        if (!config.getIsActive()) {
            throw new CustomException("Game is not active");
        }

        LocalTime currentTime = config.getOperatingHoursStart();
        LocalTime endTime = config.getOperatingHoursEnd();
        int duration = config.getSlotDurationMinutes();

        while (currentTime.plusMinutes(duration).isBefore(endTime) ||
                currentTime.plusMinutes(duration).equals(endTime)) {

            if (!slotRepository.existsByGameTypeAndSlotDateAndStartTime(gameType, date, currentTime)) {
                GameSlot slot = GameSlot.builder()
                        .gameType(gameType)
                        .slotDate(date)
                        .startTime(currentTime)
                        .endTime(currentTime.plusMinutes(duration))
                        .maxPlayers(config.getMaxPlayersPerSlot())
                        .status("AVAILABLE")
                        .build();

                slotRepository.save(slot);
            }

            currentTime = currentTime.plusMinutes(duration);
        }
    }

    public List<GameSlotResponse> getAvailableSlots(GameType gameType, LocalDate date) {
        List<GameSlot> slots = slotRepository.findByGameTypeAndSlotDate(gameType, date);

        return slots.stream()
                .map(this::mapToSlotResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookingResponse bookSlot(BookSlotRequest request, Long userId) {
        GameSlot slot = slotRepository.findById(request.getSlotId())
                .orElseThrow(() -> new CustomException("Slot not found"));

        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found"));

        List<GameBooking> activeBookings = bookingRepository.findActiveBookingsByUserAndDate(
                booker, slot.getSlotDate());

        if (!activeBookings.isEmpty()) {
            throw new CustomException("You already have a booking for this day");
        }

        int totalPlayers = 1;
        if (request.getParticipantUserIds() != null) {
            totalPlayers += request.getParticipantUserIds().size();
        }

        int currentPlayers = slot.getCurrentPlayerCount();
        if (currentPlayers + totalPlayers > slot.getMaxPlayers()) {
            throw new CustomException("Not enough space in this slot");
        }

        GameBooking booking = GameBooking.builder()
                .slot(slot)
                .user(booker)
                .bookingStatus("CONFIRMED")
                .build();

        GameBooking savedBooking = bookingRepository.save(booking);

        if (request.getParticipantUserIds() != null) {
            for (Long participantId : request.getParticipantUserIds()) {
                User participant = userRepository.findById(participantId)
                        .orElseThrow(() -> new CustomException("Participant not found: " + participantId));

                GameBookingParticipant participant1 = GameBookingParticipant.builder()
                        .booking(savedBooking)
                        .user(participant)
                        .notificationSent(false)
                        .calendarInviteSent(false)
                        .build();

                participantRepository.save(participant1);
            }
        }

        if (slot.getCurrentPlayerCount() >= slot.getMaxPlayers()) {
            slot.setStatus("FULL");
            slotRepository.save(slot);
        }

        return mapToBookingResponse(savedBooking);
    }

    public List<BookingResponse> getMyBookings(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found"));

        return bookingRepository.findByUser(user).stream()
                .map(this::mapToBookingResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancelBooking(Long bookingId, Long userId) {
        GameBooking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new CustomException("Booking not found"));

        if (!booking.getUser().getUserId().equals(userId)) {
            throw new CustomException("You can only cancel your own bookings");
        }

        booking.setBookingStatus("CANCELLED");
        booking.setCancelledAt(java.time.LocalDateTime.now());
        bookingRepository.save(booking);

        GameSlot slot = booking.getSlot();
        if (slot.getCurrentPlayerCount() < slot.getMaxPlayers()) {
            slot.setStatus("AVAILABLE");
            slotRepository.save(slot);
        }
    }

    private GameConfigurationResponse mapToConfigResponse(GameConfiguration config) {
        return GameConfigurationResponse.builder()
                .configId(config.getConfigId())
                .gameType(config.getGameType())
                .operatingHoursStart(config.getOperatingHoursStart())
                .operatingHoursEnd(config.getOperatingHoursEnd())
                .slotDurationMinutes(config.getSlotDurationMinutes())
                .maxPlayersPerSlot(config.getMaxPlayersPerSlot())
                .isActive(config.getIsActive())
                .build();
    }

    private GameSlotResponse mapToSlotResponse(GameSlot slot) {
        List<BookedPlayerDTO> players = slot.getBookings().stream()
                .filter(b -> !"CANCELLED".equals(b.getBookingStatus()))
                .map(b -> BookedPlayerDTO.builder()
                        .userId(b.getUser().getUserId())
                        .fullName(b.getUser().getFirstName() + " " + b.getUser().getLastName())
                        .email(b.getUser().getEmail())
                        .build())
                .collect(Collectors.toList());

        return GameSlotResponse.builder()
                .slotId(slot.getSlotId())
                .gameType(slot.getGameType())
                .slotDate(slot.getSlotDate())
                .startTime(slot.getStartTime())
                .endTime(slot.getEndTime())
                .maxPlayers(slot.getMaxPlayers())
                .currentPlayers(slot.getCurrentPlayerCount())
                .status(slot.getStatus())
                .bookedPlayers(players)
                .build();
    }

    private BookingResponse mapToBookingResponse(GameBooking booking) {
        List<String> participantNames = booking.getParticipants().stream()
                .map(p -> p.getUser().getFirstName() + " " + p.getUser().getLastName())
                .collect(Collectors.toList());

        return BookingResponse.builder()
                .bookingId(booking.getBookingId())
                .slotId(booking.getSlot().getSlotId())
                .gameType(booking.getSlot().getGameType())
                .slotDate(booking.getSlot().getSlotDate())
                .startTime(booking.getSlot().getStartTime())
                .endTime(booking.getSlot().getEndTime())
                .bookedByName(booking.getUser().getFirstName() + " " + booking.getUser().getLastName())
                .participantNames(participantNames)
                .status(booking.getBookingStatus())
                .bookedAt(booking.getBookedAt())
                .build();
    }
}
