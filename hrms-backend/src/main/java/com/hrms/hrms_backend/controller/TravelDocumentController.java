package com.hrms.hrms_backend.controller;
import com.hrms.hrms_backend.dto.travel.TravelDocumentResponse;
import com.hrms.hrms_backend.entity.User;
import com.hrms.hrms_backend.service.TravelDocumentService;
import com.hrms.hrms_backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("api/travels/document")
public class TravelDocumentController {

    private final TravelDocumentService travelDocumentService;
    private final UserService userService;

    public TravelDocumentController(TravelDocumentService travelDocumentService, UserService userService) {
        this.travelDocumentService = travelDocumentService;
        this.userService = userService;
    }

    @PostMapping("/{travelId}/documents")
    public ResponseEntity<TravelDocumentResponse> uploadTravelDocument(
            @PathVariable Long travelId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("documentType") String documentType,
            @RequestParam(value = "userId", required = false) Long userId
    ) {
        User user = getCurrentUser();
        String role = user.getRole().toString();

        TravelDocumentResponse response = travelDocumentService.uploadTravelDocument(
                travelId, file, documentType, userId, user.getUserId(), role
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{travelId}/documents")
    public ResponseEntity<List<TravelDocumentResponse>> getTravelDocuments(
            @PathVariable Long travelId
    ) {
        User user = getCurrentUser();
        String role = user.getRole().toString();

        List<TravelDocumentResponse> documents = travelDocumentService.getTravelDocuments(
                travelId, user.getUserId(), role
        );

        return ResponseEntity.ok(documents);
    }



    private User getCurrentUser() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findUserByEmail(userEmail);
    }

}
