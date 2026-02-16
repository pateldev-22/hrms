package com.hrms.hrms_backend.dto.job;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReferralResponse {
    private Long referralId;
    private Long jobId;
    private String jobTitle;
    private String friendName;
    private String friendEmail;
    private String cvFilePath;
    private String referralNote;
    private String referredByName;
    private LocalDateTime createdAt;
}

