package com.hrms.hrms_backend.dto.job;

import com.hrms.hrms_backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobPostingRequest {
    private String jobTitle;
    private String department;
    private String location;
    private String experienceRequired;
    private String jobSummary;
    private String jobDescription;
    private String hrOwnerEmail;
    private String cvReviewerEmails;
    private LocalDate closingDate;
    private LocalDateTime createdAt;
}
