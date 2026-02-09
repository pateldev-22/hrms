package com.hrms.hrms_backend.mapper;

import com.hrms.hrms_backend.dto.travel.TravelAssignmentResponse;
import com.hrms.hrms_backend.dto.travel.TravelPlanResponse;
import com.hrms.hrms_backend.entity.TravelAssignment;
import com.hrms.hrms_backend.entity.TravelPlan;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TravelMapper {
    public TravelPlanResponse toDto(TravelPlan travelPlan){
        if(travelPlan == null) return null;

        TravelPlanResponse dto = new TravelPlanResponse();

        dto.setTravelId(travelPlan.getTravelId());
        dto.setTravelName(travelPlan.getTravelName());
        dto.setDestination(travelPlan.getDestination());
        dto.setPurpose(travelPlan.getPurpose());
        dto.setStartDate(travelPlan.getStartDate());
        dto.setEndDate(travelPlan.getEndDate());
        dto.setCreatedByName(travelPlan.getCreatedByHr().getFirstName());
        dto.setCreatedByHrId(travelPlan.getCreatedByHr().getUserId());
        dto.setCreatedAt(travelPlan.getCreatedAt());
        dto.setUpdatedAt(travelPlan.getUpdatedAt());
        List<TravelAssignmentResponse> assignments = travelPlan.getTravelAssignments().stream()
                .map(this::mapAssignmentToResponse)
                .collect(Collectors.toList());
        dto.setAssignments(assignments);


        return dto;
    }

    private TravelAssignmentResponse mapAssignmentToResponse(TravelAssignment assignment) {
        TravelAssignmentResponse response = new TravelAssignmentResponse();
        response.setAssignmentId(assignment.getAssignmentId());
        response.setEmployeeId(assignment.getUser().getUserId());
        response.setEmployeeName(assignment.getUser().getFirstName());
        response.setEmployeeEmail(assignment.getUser().getEmail());
        response.setDepartment(assignment.getUser().getDepartment());
        response.setAssignmentStatus(assignment.getAssignmentStatus().name());
        return response;
    }
}
