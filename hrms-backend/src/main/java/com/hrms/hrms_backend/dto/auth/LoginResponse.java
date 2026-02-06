package com.hrms.hrms_backend.dto.auth;
import com.hrms.hrms_backend.constants.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String accessToken;

    private String refreshToken;

    private Long expiresIn;
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
}

