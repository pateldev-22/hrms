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


@Service
@Slf4j
public class TravelService {

    private final TravelPlanRepository travelPlanRepository;
    private final UserRepository userRepository;
    private final TravelMapper travelMapper;

    @Autowired
    public TravelService(TravelPlanRepository travelPlanRepository, TravelAssignmentRepository travelAssignmentRepository, UserRepository userRepository, TravelMapper travelMapper) {
        this.travelPlanRepository = travelPlanRepository;
        this.userRepository = userRepository;
        this.travelMapper = travelMapper;
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



}

