package com.hrms.hrms_backend.entity;
import com.hrms.hrms_backend.constants.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false, length = 255)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(length = 20)
    private String phone;

    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private LocalDate dateOfJoining;

    @Column(length = 500)
    private String profilePhotoUrl;

    @Column(length = 100)
    private String department;

    @Column(length = 100)
    private String designation;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.EMPLOYEE;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

}

