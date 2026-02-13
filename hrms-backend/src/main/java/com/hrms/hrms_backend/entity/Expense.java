package com.hrms.hrms_backend.entity;

import com.hrms.hrms_backend.constants.ExpenseStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "expenses")
@Builder
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseId;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "expense_date")
    private LocalDate expenseDate;

    private String description;

    private ExpenseStatus status;

    @Column(name = "hr_remarks")
    private String hrRemarks;

    @ManyToOne
    @JoinColumn(name = "travel_id")
    private TravelPlan travelPlan;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ExpenseCategory expenseCategory;

    @ManyToOne
    @JoinColumn(name = "reviewed_by")
    private User reviewedBy;

    @OneToMany(mappedBy = "expense",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExpenseProofDocument> expenseProofDocuments = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at",updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updateAt;

    public void setCategory(ExpenseCategory category) {
        this.expenseCategory = category;
    }
}
