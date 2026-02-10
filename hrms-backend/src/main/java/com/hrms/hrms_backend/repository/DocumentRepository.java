package com.hrms.hrms_backend.repository;

import com.hrms.hrms_backend.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document,Long> {
    Optional<Document> findByCloudinaryPublicId(String publicId);
}

