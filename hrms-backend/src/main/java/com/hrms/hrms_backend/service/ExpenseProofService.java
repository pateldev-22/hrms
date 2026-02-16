package com.hrms.hrms_backend.service;

import com.hrms.hrms_backend.dto.expense.ExpenseProofResponse;
import com.hrms.hrms_backend.entity.Document;
import com.hrms.hrms_backend.entity.Expense;
import com.hrms.hrms_backend.entity.ExpenseProofDocument;
import com.hrms.hrms_backend.entity.User;
import com.hrms.hrms_backend.exception.CustomException;
import com.hrms.hrms_backend.repository.ExpenseProofDocumentRepository;
import com.hrms.hrms_backend.repository.ExpenseRepository;
import com.hrms.hrms_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseProofService {

    private final ExpenseProofDocumentRepository proofRepository;
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final DocumentService documentService;

    @Autowired
    public ExpenseProofService(
            ExpenseProofDocumentRepository proofRepository,
            ExpenseRepository expenseRepository,
            UserRepository userRepository,
            DocumentService documentService) {
        this.proofRepository = proofRepository;
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.documentService = documentService;
    }

    @Transactional
    public ExpenseProofResponse uploadProof(Long expenseId, MultipartFile file, Long userId) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new CustomException("Expense not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found"));

        Document document = documentService.uploadDocument(file, "EXPENSE_PROOF", user);

        ExpenseProofDocument proof = new ExpenseProofDocument();
        proof.setExpense(expense);
        proof.setDocument(document);

        ExpenseProofDocument savedProof = proofRepository.save(proof);

        return toProofResponse(savedProof);
    }

    public List<ExpenseProofResponse> getExpenseProofs(Long expenseId, Long userId, String role) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new CustomException("Expense not found"));

        if (!"HR".equalsIgnoreCase(role) && !expense.getUser().getUserId().equals(userId)) {
            throw new CustomException("Access denied");
        }

        List<ExpenseProofDocument> proofs = proofRepository.findByExpense(expense);

        return proofs.stream()
                .map(this::toProofResponse)
                .collect(Collectors.toList());
    }



    private ExpenseProofResponse toProofResponse(ExpenseProofDocument proof) {
        ExpenseProofResponse response = new ExpenseProofResponse();
        response.setProofId(proof.getProofId());
        response.setExpenseId(proof.getExpense().getExpenseId());
        response.setDocumentId(proof.getDocument().getDoucmentId());
        response.setFileName(proof.getDocument().getFileName());
        response.setFileUrl(proof.getDocument().getFilePath());
        response.setUploadedAt(proof.getDocument().getUploadedAt());
        return response;
    }
}
