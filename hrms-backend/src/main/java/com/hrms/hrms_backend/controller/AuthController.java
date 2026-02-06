package com.hrms.hrms_backend.controller;
import com.hrms.hrms_backend.dto.auth.LoginRequest;
import com.hrms.hrms_backend.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequest dto) {
        System.out.println("here !");

        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
            );
            if(authentication.isAuthenticated()){ System.out.println("hiii");}
            return jwtUtil.generateToken(dto.getEmail());
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }
    }

}
