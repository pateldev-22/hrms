package com.hrms.hrms_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "expense_proofs_document")
public class ExpenseProofDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long proofId;

    @ManyToOne
    @JoinColumn(name = "expense_id")
    private Expense expense;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "document_id")
    private Document document;
}
