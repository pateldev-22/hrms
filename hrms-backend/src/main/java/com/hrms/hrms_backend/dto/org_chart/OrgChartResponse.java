package com.hrms.hrms_backend.dto.org_chart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrgChartResponse {
    private Long userId;
    private String Email;
    private String firstName;
    private String lastName;
    private String designation;
    private String department;
    private String profilePhotoUrl;
    private Long managerId;
    private Integer level;
}
