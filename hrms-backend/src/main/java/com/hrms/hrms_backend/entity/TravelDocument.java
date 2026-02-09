package com.hrms.hrms_backend.entity;

import com.hrms.hrms_backend.constants.OwnerType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "travel_documents")
public class TravelDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "travel_document_id")
    private Long travelDocumentId;

    @ManyToOne
    @JoinColumn(name = "travel_id",nullable = false)
    private TravelPlan travelPlan;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "uploaded_by",nullable = false)
    private User uploadedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "owner_type", nullable = false)
    private OwnerType ownerType;

    @Column(name = "document_type")
    private String documentType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

}
