package com.hrms.hrms_backend.dto.travel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TravelDocumentRequest {
    private String documentType;
    private Long userId;
}
