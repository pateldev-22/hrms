package com.hrms.hrms_backend.service;

import com.hrms.hrms_backend.constants.OwnerType;
import com.hrms.hrms_backend.dto.travel.TravelDocumentResponse;
import com.hrms.hrms_backend.entity.Document;
import com.hrms.hrms_backend.entity.TravelDocument;
import com.hrms.hrms_backend.entity.TravelPlan;
import com.hrms.hrms_backend.entity.User;
import com.hrms.hrms_backend.exception.CustomException;
import com.hrms.hrms_backend.repository.TravelDocumentRepository;
import com.hrms.hrms_backend.repository.TravelPlanRepository;
import com.hrms.hrms_backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TravelDocumentService {

    private final TravelPlanRepository travelPlanRepository;
    private final UserRepository userRepository;
    private final TravelDocumentRepository travelDocumentRepository;
    private final DocumentService documentService;

    public TravelDocumentService(TravelPlanRepository travelPlanRepository, UserRepository userRepository, TravelDocumentRepository travelDocumentRepository, DocumentService documentService) {
        this.travelPlanRepository = travelPlanRepository;
        this.userRepository = userRepository;
        this.travelDocumentRepository = travelDocumentRepository;
        this.documentService = documentService;
    }

    @Transactional
    public TravelDocumentResponse uploadTravelDocument(
            Long travelId,
            MultipartFile file,
            String documentType,
            Long userId,
            Long uploaderId,
            String uploaderRole
    ) {
        TravelPlan travel = travelPlanRepository.findById(travelId)
                .orElseThrow(() -> new CustomException("Travel plan not found"));

        User uploader = userRepository.findById(uploaderId)
                .orElseThrow(() -> new CustomException("Uploader not found"));

        OwnerType ownerType;
        User documentUser;

        if ("HR".equalsIgnoreCase(uploaderRole)) {
            ownerType = OwnerType.HR;

            if (userId != null) {
                documentUser = userRepository.findById(userId)
                        .orElseThrow(() -> new CustomException("User not found"));
            } else {
                throw new CustomException("Pass UserID of employee for whom you want to upload document");
            }
        } else {
            ownerType = OwnerType.EMPLOYEE;
            documentUser = uploader;
        }

        Document document = documentService.uploadDocument(file, "TRAVEL_DOCUMENT", uploader);

        TravelDocument travelDocument = new TravelDocument();
        travelDocument.setTravelPlan(travel);
        travelDocument.setUser(documentUser);
        travelDocument.setUploadedBy(uploader);
        travelDocument.setOwnerType(ownerType);
        travelDocument.setDocumentType(documentType);
        travelDocument.setDocument(document);

        TravelDocument savedTravelDoc = travelDocumentRepository.save(travelDocument);

        return mapDocumentToResponse(savedTravelDoc);
    }

    public List<TravelDocumentResponse> getTravelDocuments(Long travelId, Long userId, String role) {
        TravelPlan travel = travelPlanRepository.findById(travelId)
                .orElseThrow(() -> new CustomException("Travel plan not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found"));

        List<TravelDocument> documents;

        if ("HR".equalsIgnoreCase(role)) {
            documents = travelDocumentRepository.findByTravelPlan(travel);
        } else {
            documents = travelDocumentRepository.findByTravelPlanAndUser(travel, user);
        }

        return documents.stream()
                .map(this::mapDocumentToResponse)
                .collect(Collectors.toList());
    }


    private TravelDocumentResponse mapDocumentToResponse(TravelDocument travelDoc) {
        TravelDocumentResponse response = new TravelDocumentResponse();
        response.setTravelDocumentId(travelDoc.getTravelDocumentId());
        response.setTravelId(travelDoc.getTravelPlan().getTravelId());
        response.setTravelName(travelDoc.getTravelPlan().getTravelName());
        response.setUserId(travelDoc.getUser().getUserId());
        response.setUserName(travelDoc.getUser().getFirstName());
        response.setUploadedByName(travelDoc.getUploadedBy().getFirstName());
        response.setOwnerType(travelDoc.getOwnerType().name());
        response.setDocumentType(travelDoc.getDocumentType());

        Document doc = travelDoc.getDocument();
        response.setDocumentId(doc.getDoucmentId());
        response.setFileName(doc.getFileName());
        response.setFileUrl(doc.getFilePath());
        response.setUploadedAt(doc.getUploadedAt());

        return response;
    }
}
