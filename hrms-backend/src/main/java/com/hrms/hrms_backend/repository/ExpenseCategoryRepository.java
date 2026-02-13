package com.hrms.hrms_backend.repository;

import com.hrms.hrms_backend.entity.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, Long> {

    List<ExpenseCategory> findByIsActiveTrue();
    List<ExpenseCategory> findByIsActiveTrueOrderByCategoryNameAsc();

    Optional<ExpenseCategory> findByCategoryName(String categoryName);
    boolean existsByCategoryNameIgnoreCase(String categoryName);
}

