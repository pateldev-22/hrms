package com.hrms.hrms_backend.dto.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookedPlayerDTO {

    private Long userId;
    private String fullName;
    private String email;
}
