package com.hrms.hrms_backend.controller;
import com.hrms.hrms_backend.dto.expense.expenseCategory.ExpenseCategoryRequest;
import com.hrms.hrms_backend.dto.expense.expenseCategory.ExpenseCategoryResponse;
import com.hrms.hrms_backend.service.ExpenseCategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expense-categories")
public class ExpenseCategoryController {

    private final ExpenseCategoryService categoryService;

    public ExpenseCategoryController(ExpenseCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<ExpenseCategoryResponse> createCategory(
            @Valid @RequestBody ExpenseCategoryRequest request) {

        ExpenseCategoryResponse response = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseCategoryResponse>> getAllActiveCategories() {
        List<ExpenseCategoryResponse> categories = categoryService.getAllActiveCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<List<ExpenseCategoryResponse>> getAllCategories() {
        List<ExpenseCategoryResponse> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ExpenseCategoryResponse> getCategoryById(@PathVariable Long categoryId) {
        ExpenseCategoryResponse category = categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok(category);
    }

    @PutMapping("/{categoryId}")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<ExpenseCategoryResponse> updateCategory(
            @PathVariable Long categoryId,
            @Valid @RequestBody ExpenseCategoryRequest request) {

        ExpenseCategoryResponse response = categoryService.updateCategory(categoryId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<Void> deactivateCategory(@PathVariable Long categoryId) {
        categoryService.deactivateCategory(categoryId);
        return ResponseEntity.noContent().build();
    }
}
