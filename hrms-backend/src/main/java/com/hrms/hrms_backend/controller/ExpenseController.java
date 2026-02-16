package com.hrms.hrms_backend.controller;

import com.hrms.hrms_backend.dto.expense.*;
import com.hrms.hrms_backend.entity.User;
import com.hrms.hrms_backend.service.ExpenseProofService;
import com.hrms.hrms_backend.service.ExpenseService;
import com.hrms.hrms_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final ExpenseProofService proofService;
    private final UserService userService;

    @Autowired
    public ExpenseController(ExpenseService expenseService, ExpenseProofService proofService, UserService userService) {
        this.expenseService = expenseService;
        this.proofService = proofService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(
            @Valid @RequestBody ExpenseCreateRequest request) {
        User user = getCurrentUser();
        ExpenseResponse response = expenseService.createExpense(request, user.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getAllExpenses() {
        User user = getCurrentUser();
        String role = user.getRole().toString();
        List<ExpenseResponse> expenses = expenseService.getExpensesForUser(user.getUserId(), role);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/{expenseId}")
    public ResponseEntity<ExpenseResponse> getExpenseById(@PathVariable Long expenseId) {
        User user = getCurrentUser();
        String role = user.getRole().toString();
        ExpenseResponse response = expenseService.getExpenseById(expenseId, user.getUserId(), role);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/travel/{travelId}")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByTravel(@PathVariable Long travelId) {
        User user = getCurrentUser();
        String role = user.getRole().toString();
        List<ExpenseResponse> expenses = expenseService.getExpensesByTravel(travelId, user.getUserId(), role);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<List<ExpenseResponse>> getPendingExpenses() {
        List<ExpenseResponse> expenses = expenseService.getPendingExpenses();
        return ResponseEntity.ok(expenses);
    }

    @PutMapping("/{expenseId}")
    public ResponseEntity<ExpenseResponse> updateExpense(
            @PathVariable Long expenseId,
            @Valid @RequestBody ExpenseUpdateRequest request) {
        User user = getCurrentUser();
        ExpenseResponse response = expenseService.updateExpense(expenseId, request, user.getUserId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long expenseId) {
        User user = getCurrentUser();
        expenseService.deleteExpense(expenseId, user.getUserId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{expenseId}/review")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<ExpenseResponse> reviewExpense(
            @PathVariable Long expenseId,
            @Valid @RequestBody ExpenseReviewRequest request) {
        User user = getCurrentUser();
        ExpenseResponse response = expenseService.reviewExpense(expenseId, request, user.getUserId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{expenseId}/proofs")
    public ResponseEntity<ExpenseProofResponse> uploadProof(
            @PathVariable Long expenseId,
            @RequestParam("file") MultipartFile file) {
        User user = getCurrentUser();
        ExpenseProofResponse response = proofService.uploadProof(expenseId, file, user.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{expenseId}/proofs")
    public ResponseEntity<List<ExpenseProofResponse>> getExpenseProofs(@PathVariable Long expenseId) {
        User user = getCurrentUser();
        String role = user.getRole().toString();
        List<ExpenseProofResponse> proofs = proofService.getExpenseProofs(expenseId, user.getUserId(), role);
        return ResponseEntity.ok(proofs);
    }

    @PutMapping("/{expenseId}/submit")
    public ResponseEntity<ExpenseResponse> submitExpense(@PathVariable Long expenseId) {
        User user = getCurrentUser();
        ExpenseResponse response = expenseService.submitExpense(expenseId, user.getUserId());
        return ResponseEntity.ok(response);
    }

    private User getCurrentUser() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findUserByEmail(userEmail);
    }
}
