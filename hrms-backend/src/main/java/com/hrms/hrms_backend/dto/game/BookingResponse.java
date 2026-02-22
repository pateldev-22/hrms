package com.hrms.hrms_backend.dto.game;


import com.hrms.hrms_backend.constants.GameType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {

    private Long bookingId;
    private Long slotId;
    private GameType gameType;
    private LocalDate slotDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String bookedByName;
    private List<String> participantNames;
    private String status;
    private LocalDateTime bookedAt;
}
