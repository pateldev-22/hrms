package com.hrms.hrms_backend.controller;
import com.hrms.hrms_backend.dto.auth.AuthResponse;
import com.hrms.hrms_backend.dto.auth.LoginRequest;
import com.hrms.hrms_backend.dto.auth.RefreshTokenRequest;
import com.hrms.hrms_backend.dto.auth.RegisterRequest;
import com.hrms.hrms_backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request,@CookieValue(value="refreshToken",defaultValue = "dev") String refresh_token) {
        AuthResponse detail = (authService.login(request,refresh_token));

        ResponseCookie cookie = ResponseCookie.from("refreshToken",detail.getRefreshToken())
                .httpOnly(true)
                .path("/")
                .maxAge(Duration.ofDays(7))
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,cookie.toString())
                .body(detail);
    }


    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(Authentication authentication){
        String email = authentication.getName();
        authService.logout(email);

        ResponseCookie cookie = ResponseCookie.from("refreshToken","")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,cookie.toString())
                .body("Logged Out Successfully");
    }


}
