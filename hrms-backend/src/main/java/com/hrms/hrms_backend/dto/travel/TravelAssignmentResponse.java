package com.hrms.hrms_backend.dto.travel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TravelAssignmentResponse {
    private Long assignmentId;
    private Long employeeId;
    private String employeeName;
    private String employeeEmail;
    private String department;
    private String assignmentStatus;
}
