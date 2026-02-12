package com.hrms.hrms_backend.service;
import com.hrms.hrms_backend.dto.auth.*;
import com.hrms.hrms_backend.entity.RefreshToken;
import com.hrms.hrms_backend.entity.User;
import com.hrms.hrms_backend.exception.CustomException;
import com.hrms.hrms_backend.repository.UserRepository;
import com.hrms.hrms_backend.security.JwtUtil;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Collection;


@Service
public class AuthService {


    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;


    public AuthService(
            UserRepository userRepository,
            PasswordEncoder encoder,
            AuthenticationManager authManager,
            JwtUtil jwtUtil,
            RefreshTokenService refreshTokenService) {


        this.userRepository = userRepository;
        this.encoder = encoder;
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
    }


    public void register(RegisterRequest req) {


        if (userRepository.existsByEmail(req.getEmail())) {
            throw new CustomException("Email already exists");
        }


        User user = User.builder()
                .email(req.getEmail())
                .passwordHash(encoder.encode(req.getPassword()))
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .phone(req.getPhone())
                .dateOfBirth(req.getDateOfBirth())
                .dateOfJoining(req.getDateOfJoining())
                .department(req.getDepartment())
                .designation(req.getDesignation())
                .role(req.getRole())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();


        userRepository.save(user);
    }


    public AuthResponse login(LoginRequest req,String refresh_token) {


        Authentication authentication =
                authManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                req.getEmail(), req.getPassword()
                        )
                );


        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new CustomException("User not found"));


        String accessToken = jwtUtil.generateToken(
                req.getEmail(),
                authentication.getAuthorities()
        );


        if(refresh_token != null && !refresh_token.equals("dev")){
            refreshTokenService.verifyRefreshToken(refresh_token);
            return new AuthResponse(
                    accessToken,
                    refresh_token,
                    user.getEmail(),
                    user.getRole().name()
            );
        }else {
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);


            return new AuthResponse(
                    accessToken,
                    refreshToken.getToken(),
                    user.getEmail(),
                    user.getRole().name()
            );
        }
    }


    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenService
                .verifyRefreshToken(request.getRefreshToken());


        User user = refreshToken.getUser();


        Collection<? extends GrantedAuthority> authorities =
                org.springframework.security.core.userdetails.User
                        .withUsername(user.getEmail())
                        .password(user.getPasswordHash())
                        .roles(user.getRole().name())
                        .build()
                        .getAuthorities();


        String newAccessToken = jwtUtil.generateToken(
                user.getEmail(),
                authorities
        );


        return new AuthResponse(
                newAccessToken,
                refreshToken.getToken(),
                user.getEmail(),
                user.getRole().name()
        );
    }

    public void logout(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found"));

        refreshTokenService.deleteByUser(user);
    }


}
