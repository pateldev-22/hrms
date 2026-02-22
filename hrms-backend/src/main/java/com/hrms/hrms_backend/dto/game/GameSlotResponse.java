package com.hrms.hrms_backend.dto.game;

import com.hrms.hrms_backend.constants.GameType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameSlotResponse {

    private Long slotId;
    private GameType gameType;
    private LocalDate slotDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer maxPlayers;
    private Integer currentPlayers;
    private String status;
    private List<BookedPlayerDTO> bookedPlayers;
}
