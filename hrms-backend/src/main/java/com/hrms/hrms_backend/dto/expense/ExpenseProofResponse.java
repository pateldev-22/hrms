package com.hrms.hrms_backend.dto.expense;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ExpenseProofResponse {
    private Long proofId;
    private Long expenseId;
    private Long documentId;
    private String fileName;
    private String fileUrl;
    private String fileType;
    private Long fileSize;
    private String readableFileSize;
    private LocalDateTime uploadedAt;
}
