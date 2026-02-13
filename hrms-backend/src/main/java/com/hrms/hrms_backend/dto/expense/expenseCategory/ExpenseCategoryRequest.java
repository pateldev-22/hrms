package com.hrms.hrms_backend.dto.expense.expenseCategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseCategoryRequest {
    private String categoryName;
    private Integer maxAmountPerDay;
    private String description;
    private boolean isActive;
}

