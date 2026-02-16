package com.hrms.hrms_backend.dto.expense;

import com.hrms.hrms_backend.entity.ExpenseCategory;
import com.hrms.hrms_backend.entity.TravelPlan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseCreateRequest {
    private Long travelId;
    private String expenseCategoryName;
    private Integer amount;
    private LocalDate expenseDate;
    private String description;
}
