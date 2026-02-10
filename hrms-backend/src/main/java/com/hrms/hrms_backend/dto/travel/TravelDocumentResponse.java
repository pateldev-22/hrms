package com.hrms.hrms_backend.dto.travel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TravelDocumentResponse {
    private Long travelDocumentId;
    private Long travelId;
    private String travelName;
    private Long userId;
    private String userName;
    private String uploadedByName;
    private String ownerType;
    private String documentType;
    private Long documentId;
    private String fileName;
    private String fileUrl;
    private LocalDateTime uploadedAt;
}
