package com.hrms.hrms_backend.dto.expense;

import com.hrms.hrms_backend.constants.ExpenseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
public class ExpenseResponse {
    private Long expenseId;
    private Long travelId;
    private Long userId;
    private Long categoryId;
    private Long reviewedBy;
    private Integer amount;
    private LocalDate expenseDate;
    private String description;
    private ExpenseStatus status;
    private String hrRemarks;
}
