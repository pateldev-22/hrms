package com.hrms.hrms_backend.dto.game;

import com.hrms.hrms_backend.constants.GameType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameConfigurationResponse {

    private Long configId;
    private GameType gameType;
    private LocalTime operatingHoursStart;
    private LocalTime operatingHoursEnd;
    private Integer slotDurationMinutes;
    private Integer maxPlayersPerSlot;
    private Boolean isActive;
}
