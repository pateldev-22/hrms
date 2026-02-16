package com.hrms.hrms_backend.dto.expense;
import com.hrms.hrms_backend.constants.ExpenseStatus;
import lombok.Data;

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
    private Integer proofCount;

    private String categoryName;
    private String employeeName;
    private String employeeEmail;
    private String travelName;
}
