package com.hrms.hrms_backend.dto.expense.expenseCategory;

import lombok.Data;

@Data
public class ExpenseCategoryResponse {
    private Long expenseCategoryId;
    private String categoryName;
    private Integer maxAmountPerDay;
    private String description;
}
