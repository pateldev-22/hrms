package com.hrms.hrms_backend.dto.job;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class JobPostingResponse {
    private Long jobId;
    private String jobTitle;
    private String department;
    private String location;
    private String experienceRequired;
    private String jobSummary;
    private String jobDescription;
    private String status;
    private LocalDate closingDate;
    private Integer totalReferrals;
    private Integer totalShares;
    private LocalDateTime createdAt;
    private String jdFilePath;
    private boolean hasJD;
}

