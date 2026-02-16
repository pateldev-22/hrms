package com.hrms.hrms_backend.dto.job;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReferralRequest {
    @NotBlank(message = "Friend name is required")
    private String friendName;
    private String friendEmail;
    private String referralNote;
}
