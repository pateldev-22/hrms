package com.hrms.hrms_backend.dto.travel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TravelPlanResponse {
    private Long travelId;
    private String travelName;
    private String destination;
    private String purpose;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String createdByName;
    private Long createdByHrId;
    private List<TravelAssignmentResponse> assignments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}