package com.hrms.hrms_backend.repository;

import com.hrms.hrms_backend.constants.ExpenseStatus;
import com.hrms.hrms_backend.entity.Expense;
import com.hrms.hrms_backend.entity.TravelPlan;
import com.hrms.hrms_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUserOrderByExpenseDateDesc(User employee);

    List<Expense> findByTravelPlanOrderByExpenseDateDesc(TravelPlan travelPlan);

    List<Expense> findByStatusOrderByCreatedAtAsc(ExpenseStatus status);

    List<Expense> findByTravelPlanAndUser(TravelPlan travelPlan, User employee);

    @Query("SELECT SUM(e.amount) FROM Expense e " +
            "WHERE e.user = :employee " +
            "AND e.expenseCategory.categoryId = :categoryId " +
            "AND e.expenseDate = :date ")
    Integer sumAmountByCategoryAndDate(
            @Param("employee") User employee,
            @Param("categoryId") Long categoryId,
            @Param("date") LocalDate date
    );

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.travelPlan = :travelPlan AND e.status = :status")
    Integer sumAmountByTravelPlanAndStatus(
            @Param("travelPlan") TravelPlan travelPlan,
            @Param("status") ExpenseStatus status
    );
}

