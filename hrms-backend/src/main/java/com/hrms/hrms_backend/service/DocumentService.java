package com.hrms.hrms_backend.service;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.hrms.hrms_backend.entity.Document;
import com.hrms.hrms_backend.entity.User;
import com.hrms.hrms_backend.exception.CustomException;
import com.hrms.hrms_backend.repository.DocumentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final Cloudinary cloudinary;

    @Value("${cloudinary.folder.travel-documents}") String travel_docs_store_loc;
    @Value("${cloudinary.folder.expense-proofs}") String expense_docs_store_loc;
    @Value("${cloudinary.folder.refered-cv}") String cv_docs_store_loc;

    @Autowired
    public DocumentService(DocumentRepository documentRepository, Cloudinary cloudinary) {
        this.documentRepository = documentRepository;
        this.cloudinary = cloudinary;
    }

    @Transactional
    public Document uploadDocument(MultipartFile file, String category, User uploadedBy) {

        try {
            String publicId = cloudinary.randomPublicId();
            String folder = getFolder(category);

            Map uploadParams = ObjectUtils.asMap(
                    "public_id", publicId,
                    "folder", folder,
                    "resource_type", "auto",
                    "overwrite", false
            );

            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadParams);

            Document document = new Document();
            document.setFileName(file.getOriginalFilename());
            document.setFilePath((String) uploadResult.get("secure_url"));
            document.setCloudinaryPublicId((String) uploadResult.get("public_id"));
            document.setUploadedBy(uploadedBy);

            Document savedDocument = documentRepository.save(document);

            return savedDocument;

        } catch (IOException e) {
            throw new CustomException("Failed to upload document: " + e.getMessage());
        }
    }

    private String getFolder(String category) {
        if(category.equals("TRAVEL_DOCUMENT")){
            return travel_docs_store_loc;
        }else if(category.equals("EXPENSE_PROOF")){
            return expense_docs_store_loc;
        }else{
            return cv_docs_store_loc;
        }
    }

}
