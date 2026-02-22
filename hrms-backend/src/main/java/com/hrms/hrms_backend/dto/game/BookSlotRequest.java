package com.hrms.hrms_backend.dto.game;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookSlotRequest {

    @NotNull
    private Long slotId;

    private List<Long> participantUserIds;
}

