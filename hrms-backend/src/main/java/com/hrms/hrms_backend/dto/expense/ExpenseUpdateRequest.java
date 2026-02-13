package com.hrms.hrms_backend.dto.expense;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseUpdateRequest {
    private Long categoryId;
    private Integer amount;
    private LocalDate expenseDate;
    private String description;
}
