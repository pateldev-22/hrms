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
@Slf4j
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final Cloudinary cloudinary;

    @Value("${cloudinary.folder.travel-documents}") String travel_docs_store_loc;
    @Value("${cloudinary.folder.expense-proofs}") String expense_docs_store_loc;

    @Autowired
    public DocumentService(DocumentRepository documentRepository, Cloudinary cloudinary) {
        this.documentRepository = documentRepository;
        this.cloudinary = cloudinary;
    }

    @Transactional
    public Document uploadDocument(MultipartFile file, String category, User uploadedBy) {

        try {
            String publicId = generatePublicId(file.getOriginalFilename());
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

            log.info("Document uploaded: {} by {}", file.getOriginalFilename(), uploadedBy.getEmail());

            return savedDocument;

        } catch (IOException e) {
            log.error("Failed to upload document: {}", e.getMessage());
            throw new CustomException("Failed to upload document: " + e.getMessage());
        }
    }

    private String getFolder(String category) {
        if(category.equals("TRAVEL_DOCUMENT")){
            return travel_docs_store_loc;
        }else{
            return expense_docs_store_loc;
        }
    }

    private String generatePublicId(String originalFilename) {
        String baseName = originalFilename;
        if (originalFilename.contains(".")) {
            baseName = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        }

        String timestamp = String.valueOf(System.currentTimeMillis());
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);

        return String.format("%s_%s_%s", baseName, timestamp, uniqueId);
    }



    @Transactional
    public void deleteDocument(Long documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new CustomException("Document not found"));

        try {
            if (document.getCloudinaryPublicId() != null) {
                Map deleteResult = cloudinary.uploader().destroy(
                        document.getCloudinaryPublicId(),
                        ObjectUtils.emptyMap()
                );
                log.info("Deleted from Cloudinary: {}", deleteResult.get("result"));
            }

            documentRepository.delete(document);
            log.info("Document deleted: {}", document.getFileName());

        } catch (IOException e) {
            log.error("Failed to delete document: {}", e.getMessage());
            throw new CustomException("Failed to delete document: " + e.getMessage());
        }
    }

    public Document getDocument(Long documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new CustomException("Document not found"));
    }
}
