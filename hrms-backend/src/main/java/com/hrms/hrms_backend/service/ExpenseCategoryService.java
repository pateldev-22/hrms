package com.hrms.hrms_backend.service;

import com.hrms.hrms_backend.dto.expense.expenseCategory.ExpenseCategoryRequest;
import com.hrms.hrms_backend.dto.expense.expenseCategory.ExpenseCategoryResponse;
import com.hrms.hrms_backend.entity.ExpenseCategory;
import com.hrms.hrms_backend.exception.CustomException;
import com.hrms.hrms_backend.repository.ExpenseCategoryRepository;
import jdk.jfr.Category;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseCategoryService {

    private final ExpenseCategoryRepository categoryRepository;

    public ExpenseCategoryService(ExpenseCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public ExpenseCategoryResponse createCategory(ExpenseCategoryRequest request) {

        if (categoryRepository.existsByCategoryNameIgnoreCase(request.getCategoryName())){
            throw new CustomException("Category with this name already exists");
        }

        ExpenseCategory category = ExpenseCategory.builder()
                .categoryName(request.getCategoryName())
                .maxAmountPerDay(request.getMaxAmountPerDay())
                .description(request.getDescription())
                .build();
        category.setIsActive(true);

        ExpenseCategory savedCategory = categoryRepository.save(category);

        return toCategoryDTO(savedCategory);
    }

    public List<ExpenseCategoryResponse> getAllActiveCategories() {
        return categoryRepository.findByIsActiveTrueOrderByCategoryNameAsc()
                .stream()
                .map(this::toCategoryDTO)
                .collect(Collectors.toList());
    }

    public List<ExpenseCategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::toCategoryDTO)
                .collect(Collectors.toList());
    }

    public ExpenseCategoryResponse getCategoryById(Long categoryId) {
        ExpenseCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException("Category not found"));

        return toCategoryDTO(category);
    }

    @Transactional
    public ExpenseCategoryResponse updateCategory(Long categoryId, ExpenseCategoryRequest request) {
        ExpenseCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException("Category not found"));

        category.setCategoryName(request.getCategoryName());
        category.setDescription(request.getDescription());
        category.setMaxAmountPerDay(request.getMaxAmountPerDay());

        ExpenseCategory updatedCategory = categoryRepository.save(category);
        return toCategoryDTO(updatedCategory);
    }

    @Transactional
    public void deactivateCategory(Long categoryId) {
        ExpenseCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException("Category not found"));

        category.setIsActive(false);
        categoryRepository.save(category);

    }


    public ExpenseCategoryResponse toCategoryDTO(ExpenseCategory category){
        ExpenseCategoryResponse dto = new ExpenseCategoryResponse();

        dto.setCategoryName(category.getCategoryName());
        dto.setDescription(category.getDescription());
        dto.setMaxAmountPerDay(category.getMaxAmountPerDay());

        return dto;
    }
}

