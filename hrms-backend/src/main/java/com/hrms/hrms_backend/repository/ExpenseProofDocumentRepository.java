package com.hrms.hrms_backend.repository;

import com.hrms.hrms_backend.entity.Expense;
import com.hrms.hrms_backend.entity.ExpenseProofDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseProofDocumentRepository extends JpaRepository<ExpenseProofDocument, Long> {
    List<ExpenseProofDocument> findByExpense(Expense expense);
    Long countByExpense(Expense expense);
}

