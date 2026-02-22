package com.hrms.hrms_backend.dto.game;

import com.hrms.hrms_backend.constants.GameType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameConfigurationRequest {

    @NotBlank
    private GameType gameType;

    @NotNull
    private LocalTime operatingHoursStart;

    @NotNull
    private LocalTime operatingHoursEnd;

    @NotNull
    private Integer slotDurationMinutes;

    @NotNull
    private Integer maxPlayersPerSlot;

    private Boolean isActive;
}

