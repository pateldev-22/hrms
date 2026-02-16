package com.hrms.hrms_backend.dto.user;

import lombok.Data;

@Data
public class UserResponse {
    private String Email;
    private Long userId;
    private String firstName;
    private String lastName;
    private String profilePhotoUrl;
    private String Department;
    private String role;
}
