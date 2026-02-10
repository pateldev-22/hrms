package com.hrms.hrms_backend.service;

import com.hrms.hrms_backend.constants.AssignmentStatus;
import com.hrms.hrms_backend.dto.travel.TravelPlanRequest;
import com.hrms.hrms_backend.dto.travel.TravelPlanResponse;

import com.hrms.hrms_backend.entity.*;
import com.hrms.hrms_backend.exception.CustomException;
import com.hrms.hrms_backend.mapper.TravelMapper;
import com.hrms.hrms_backend.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class TravelService {

    private final TravelPlanRepository travelPlanRepository;
    private final UserRepository userRepository;
    private final TravelMapper travelMapper;
    private final TravelAssignmentRepository travelAssignmentRepository;

    @Autowired
    public TravelService(TravelPlanRepository travelPlanRepository, TravelAssignmentRepository travelAssignmentRepository, UserRepository userRepository, TravelMapper travelMapper, TravelAssignmentRepository travelAssignmentRepository1, DocumentService documentService, TravelDocumentRepository travelDocumentRepository) {
        this.travelPlanRepository = travelPlanRepository;
        this.userRepository = userRepository;
        this.travelMapper = travelMapper;
        this.travelAssignmentRepository = travelAssignmentRepository1;
    }

    @Transactional
    public TravelPlanResponse createTravelPlan(TravelPlanRequest request,Long hrUserId) {

        User hr = userRepository.findById(hrUserId)
                .orElseThrow(() -> new CustomException("HR user not found"));

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new CustomException("End date cannot be before start date");
        }

        TravelPlan travelPlan = new TravelPlan();
        travelPlan.setTravelName(request.getTravelName());
        travelPlan.setDestination(request.getDestination());
        travelPlan.setPurpose(request.getPurpose());
        travelPlan.setStartDate(request.getStartDate());
        travelPlan.setEndDate(request.getEndDate());
        travelPlan.setCreatedByHr(hr);

        TravelPlan savedTravel = travelPlanRepository.save(travelPlan);

        for (Long userId : request.getUserIds()) {
            User employee = userRepository.findById(userId)
                    .orElseThrow(() -> new CustomException("User not found: " + userId));

            TravelAssignment assignment = new TravelAssignment();
            assignment.setTravelPlan(savedTravel);
            assignment.setUser(employee);
            assignment.setAssignmentStatus(AssignmentStatus.ASSIGNED);
            savedTravel.addAssignment(assignment);

            //aa future ma implement karvanu che
//            notificationService.createTravelAssignmentNotification(savedTravel, employee);

        }

        travelPlanRepository.save(savedTravel);

        return travelMapper.toDto(savedTravel);
    }

    public List<TravelPlanResponse> getTravelsForUser(Long userId, String role) {
        List<TravelPlan> travels;

        if ("HR".equalsIgnoreCase(role)) {
            travels = travelPlanRepository.findByIsDeletedFalse();
        } else {
            travels = travelPlanRepository.findByAssignedEmployee(userId);
        }

        return travels.stream()
                .map(travelMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<TravelPlanResponse> getUpcomingTravels(Long userId) {
        List<TravelPlan> travels = travelPlanRepository.findUpcomingTravelsByEmployee(
                userId
        );

        return travels.stream()
                .map(travelMapper::toDto)
                .collect(Collectors.toList());
    }


    public List<TravelPlanResponse> getPastTravels(Long userId) {
        List<TravelPlan> allTravels = travelPlanRepository.findByAssignedEmployee(userId);

        List<TravelPlan> pastTravels = allTravels.stream()
                .filter(travel -> travel.getEndDate().isBefore(LocalDate.now()))
                .collect(Collectors.toList());

        return pastTravels.stream()
                .map(travelMapper::toDto)
                .collect(Collectors.toList());
    }


    public List<TravelPlanResponse> getActiveTravels(Long userId, String role) {
        List<TravelPlan> travels;

        if ("HR".equalsIgnoreCase(role)) {
            travels = travelPlanRepository.findActiveTravels(LocalDate.now());
        } else {
            // Get employee's active travels
            List<TravelPlan> allTravels = travelPlanRepository.findByAssignedEmployee(userId);
            LocalDate today = LocalDate.now();

            travels = allTravels.stream()
                    .filter(travel ->
                            !travel.getStartDate().isAfter(today) &&
                                    !travel.getEndDate().isBefore(today)
                    )
                    .collect(Collectors.toList());
        }

        return travels.stream()
                .map(travelMapper::toDto)
                .collect(Collectors.toList());
    }


    @Transactional
    public TravelPlanResponse updateTravelPlan(Long travelId, TravelPlanRequest request, Long hrUserId) {
        TravelPlan travel = travelPlanRepository.findById(travelId)
                .orElseThrow(() -> new CustomException("Travel plan not found"));

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new CustomException("End date cannot be before start date");
        }

        travel.setTravelName(request.getTravelName());
        travel.setDestination(request.getDestination());
        travel.setPurpose(request.getPurpose());
        travel.setStartDate(request.getStartDate());
        travel.setEndDate(request.getEndDate());

        TravelPlan updatedTravel = travelPlanRepository.save(travel);

        return travelMapper.toDto(updatedTravel);
    }


    @Transactional
    public void deleteTravelPlan(Long travelId) {
        TravelPlan travel = travelPlanRepository.findById(travelId)
                .orElseThrow(() -> new CustomException("Travel plan not found"));

        travel.setDeleted(true);
        travelPlanRepository.save(travel);
    }


    @Transactional
    public void acknowledgeTravelAssignment(Long travelId, Long userId) {
        TravelPlan travel = travelPlanRepository.findById(travelId)
                .orElseThrow(() -> new CustomException("Travel plan not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found"));

        TravelAssignment assignment = travelAssignmentRepository.findByTravelPlanAndUser(travel, user)
                .orElseThrow(() -> new CustomException("Travel assignment not found"));

        assignment.setAssignmentStatus(AssignmentStatus.ACKNOWLEDGED);
        travelAssignmentRepository.save(assignment);

    }

    @Transactional
    public void cancelTravelAssignment(Long travelId, Long employeeId, Long hrUserId) {
        TravelPlan travel = travelPlanRepository.findById(travelId)
                .orElseThrow(() -> new CustomException("Travel plan not found"));

        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new CustomException("Employee not found"));

        TravelAssignment assignment = travelAssignmentRepository.findByTravelPlanAndUser(travel, employee)
                .orElseThrow(() -> new CustomException("Travel assignment not found"));

        assignment.setAssignmentStatus(AssignmentStatus.CANCELLED);
        travelAssignmentRepository.save(assignment);
    }

}

