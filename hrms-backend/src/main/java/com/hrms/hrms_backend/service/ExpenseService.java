package com.hrms.hrms_backend.service;

import com.hrms.hrms_backend.constants.ExpenseStatus;
import com.hrms.hrms_backend.dto.expense.ExpenseCreateRequest;
import com.hrms.hrms_backend.dto.expense.ExpenseResponse;
import com.hrms.hrms_backend.dto.expense.ExpenseReviewRequest;
import com.hrms.hrms_backend.dto.expense.ExpenseUpdateRequest;
import com.hrms.hrms_backend.entity.Expense;
import com.hrms.hrms_backend.entity.ExpenseCategory;
import com.hrms.hrms_backend.entity.TravelPlan;
import com.hrms.hrms_backend.entity.User;
import com.hrms.hrms_backend.exception.CustomException;
import com.hrms.hrms_backend.repository.ExpenseCategoryRepository;
import com.hrms.hrms_backend.repository.ExpenseRepository;
import com.hrms.hrms_backend.repository.TravelPlanRepository;
import com.hrms.hrms_backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseCategoryRepository categoryRepository;
    private final TravelPlanRepository travelPlanRepository;
    private final UserRepository userRepository;

    @Autowired
    public ExpenseService(
            ExpenseRepository expenseRepository,
            ExpenseCategoryRepository categoryRepository,
            TravelPlanRepository travelPlanRepository,
            UserRepository userRepository
            ) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
        this.travelPlanRepository = travelPlanRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ExpenseResponse createExpense(ExpenseCreateRequest request, Long employeeId) {

        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new CustomException("Employee not found"));

        TravelPlan travelPlan = travelPlanRepository.findById(request.getTravelId())
                .orElseThrow(() -> new CustomException("Travel plan not found"));

        ExpenseCategory category = categoryRepository.findById(request.getExpenseCategoryId())
                .orElseThrow(() -> new CustomException("Expense category not found"));

        if (!category.isActive()) {
            throw new CustomException("This expense category is inactive");
        }

        if (request.getExpenseDate().isBefore(travelPlan.getStartDate()) ||
                request.getExpenseDate().isAfter(travelPlan.getEndDate())) {
            throw new CustomException("Expense date must be within travel period");
        }

        Integer dailySpent = expenseRepository.sumAmountByCategoryAndDate(
                employee, category.getCategoryId(), request.getExpenseDate());

        if (dailySpent == null) {
            dailySpent = 0;
        }

        if (category.getMaxAmountPerDay() != null) {
            Integer totalForDay = (dailySpent += request.getAmount());
            if (totalForDay.compareTo(category.getMaxAmountPerDay()) > 0) {
                throw new CustomException(
                        String.format("Daily limit exceeded. Limit: ₹%s, Already spent: ₹%s",
                                category.getMaxAmountPerDay(), dailySpent)
                );
            }
        }

        Expense expense = Expense.builder()
                .travelPlan(travelPlan)
                .user(employee)
                .expenseCategory(category)
                .amount(request.getAmount())
                .expenseDate(request.getExpenseDate())
                .description(request.getDescription())
                .status(ExpenseStatus.PENDING)
                .build();

        Expense savedExpense = expenseRepository.save(expense);


        return toExpenseDTO(savedExpense);
    }

    public ExpenseResponse toExpenseDTO(Expense expense){
        ExpenseResponse dto = new ExpenseResponse();
        dto.setExpenseId(expense.getExpenseId());
        dto.setExpenseDate(expense.getExpenseDate());
        dto.setAmount(expense.getAmount());
        dto.setDescription(expense.getDescription());
        dto.setStatus(expense.getStatus());
        dto.setTravelId(expense.getTravelPlan().getTravelId());
        dto.setUserId(expense.getUser().getUserId());
        dto.setCategoryId(expense.getExpenseCategory().getCategoryId());
        if(expense.getHrRemarks() != null){
            dto.setHrRemarks(expense.getHrRemarks());
        }
        if(expense.getReviewedBy() != null){
            dto.setReviewedBy(expense.getReviewedBy().getUserId());
        }
        return dto;
    }

    public List<ExpenseResponse> getExpensesForUser(Long userId, String role) {
        List<Expense> expenses;

        if ("HR".equalsIgnoreCase(role)) {
            expenses = expenseRepository.findAll();
        } else {
            User employee = userRepository.findById(userId)
                    .orElseThrow(() -> new CustomException("User not found"));
            expenses = expenseRepository.findByUserOrderByExpenseDateDesc(employee);
        }

        return expenses.stream()
                .map(this::toExpenseDTO)
                .toList();
    }

    public ExpenseResponse getExpenseById(Long expenseId, Long userId, String role) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new CustomException("Expense not found"));

        if (!"HR".equalsIgnoreCase(role) &&
                !expense.getUser().getUserId().equals(userId)) {
            throw new CustomException("Access denied");
        }

        return toExpenseDTO(expense);
    }

    public List<ExpenseResponse> getExpensesByTravel(Long travelId, Long userId, String role) {
        TravelPlan travelPlan = travelPlanRepository.findById(travelId)
                .orElseThrow(() -> new CustomException("Travel plan not found"));

        List<Expense> expenses;

        if ("HR".equalsIgnoreCase(role)) {
            expenses = expenseRepository.findByTravelPlanOrderByExpenseDateDesc(travelPlan);
        } else {
            User employee = userRepository.findById(userId)
                    .orElseThrow(() -> new CustomException("User not found"));
            expenses = expenseRepository.findByTravelPlanAndUser(travelPlan, employee);
        }

        return expenses.stream()
                .map(this::toExpenseDTO)
                .toList();
    }

    public List<ExpenseResponse> getPendingExpenses() {
        List<Expense> expenses = expenseRepository.findByStatusOrderByCreatedAtAsc(ExpenseStatus.PENDING);
        return expenses.stream()
                .map(this::toExpenseDTO)
                .toList();
    }
    @Transactional
    public ExpenseResponse updateExpense(Long expenseId,ExpenseUpdateRequest request, Long userId) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new CustomException("Expense not found"));

        if (!expense.getUser().getUserId().equals(userId)) {
            throw new CustomException("You can only update your own expenses");
        }

        ExpenseCategory category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new CustomException("Category not found"));


        expense.setCategory(category);
        expense.setAmount(request.getAmount());
        expense.setExpenseDate(request.getExpenseDate());
        expense.setDescription(request.getDescription());
        expense.setStatus(ExpenseStatus.PENDING);

        Expense updatedExpense = expenseRepository.save(expense);
        return toExpenseDTO(updatedExpense);
    }

    @Transactional
    @PreAuthorize("hasRole('EMPLOYEE')")
    public void deleteExpense(Long expenseId, Long userId) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new CustomException("Expense not found"));

        if (!expense.getUser().getUserId().equals(userId)) {
            throw new CustomException("You can only delete your own expenses");
        }

        if (expense.getStatus() != ExpenseStatus.PENDING) {
            throw new CustomException("Only pending expenses can be deleted");
        }

        expenseRepository.delete(expense);
    }

    @Transactional
    public ExpenseResponse reviewExpense(Long expenseId, ExpenseReviewRequest request, Long hrUserId) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new CustomException("Expense not found"));

        User hrUser = userRepository.findById(hrUserId)
                .orElseThrow(() -> new CustomException("HR user not found"));

        if (expense.getStatus() != ExpenseStatus.PENDING) {
            throw new CustomException("Only pending expenses can be reviewed");
        }

        if (request.getStatus() == ExpenseStatus.APPROVED || request.getStatus() == ExpenseStatus.REJECTED) {
            expense.setStatus(request.getStatus());
            expense.setHrRemarks(request.getHrRemark());
            expense.setReviewedBy(hrUser);
        } else {
            throw new CustomException("Invalid review status");
        }

        Expense reviewedExpense = expenseRepository.save(expense);

        return toExpenseDTO(reviewedExpense);
    }
}

