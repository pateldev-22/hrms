package com.hrms.hrms_backend.controller;

import com.hrms.hrms_backend.constants.GameType;
import com.hrms.hrms_backend.dto.game.*;
import com.hrms.hrms_backend.entity.User;
import com.hrms.hrms_backend.service.GameService;
import com.hrms.hrms_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;
    private final UserService userService;

    public GameController(GameService gameService, UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }

    @PostMapping("/config")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<GameConfigurationResponse> createOrUpdateConfig(
            @Valid @RequestBody GameConfigurationRequest request) {
        return ResponseEntity.ok(gameService.createOrUpdateConfig(request));
    }

    @GetMapping("/config")
    public ResponseEntity<List<GameConfigurationResponse>> getAllConfigs() {
        return ResponseEntity.ok(gameService.getAllConfigs());
    }

    @PostMapping("/slots/generate")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<String> generateSlots(
            @RequestParam GameType gameType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        gameService.generateSlots(gameType, date);
        return ResponseEntity.ok("Slots generated successfully");
    }

    @GetMapping("/slots")
    public ResponseEntity<List<GameSlotResponse>> getAvailableSlots(
            @RequestParam GameType gameType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(gameService.getAvailableSlots(gameType, date));
    }

    @PostMapping("/book")
    public ResponseEntity<BookingResponse> bookSlot(@Valid @RequestBody BookSlotRequest request) {
        User user = getCurrentUser();
        return ResponseEntity.ok(gameService.bookSlot(request, user.getUserId()));
    }

    @GetMapping("/my-bookings")
    public ResponseEntity<List<BookingResponse>> getMyBookings() {
        User user = getCurrentUser();
        return ResponseEntity.ok(gameService.getMyBookings(user.getUserId()));
    }

    @DeleteMapping("/bookings/{bookingId}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingId) {
        User user = getCurrentUser();
        gameService.cancelBooking(bookingId, user.getUserId());
        return ResponseEntity.noContent().build();
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findUserByEmail(email);
    }
}








