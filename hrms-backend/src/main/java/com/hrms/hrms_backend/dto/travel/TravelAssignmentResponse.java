package com.hrms.hrms_backend.dto.travel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TravelAssignmentResponse {
    private Integer assignmentId;
    private Integer employeeId;
    private String employeeName;
    private String employeeEmail;
    private String department;
    private String assignmentStatus;
}
