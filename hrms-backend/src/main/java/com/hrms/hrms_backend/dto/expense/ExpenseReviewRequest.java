package com.hrms.hrms_backend.dto.expense;

import com.hrms.hrms_backend.constants.ExpenseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseReviewRequest {
    private ExpenseStatus status;
    private String hrRemark;
}
