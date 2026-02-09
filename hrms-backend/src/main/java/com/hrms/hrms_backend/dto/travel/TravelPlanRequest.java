package com.hrms.hrms_backend.dto.travel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TravelPlanRequest {
    @NotBlank(message = "Travel name is required")
    private String travelName;

    @NotBlank(message = "Destination is required")
    private String destination;

    private String purpose;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @NotEmpty(message = "At least one employee must be assigned")
    private List<Long> userIds;

}
