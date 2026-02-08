package com.hrms.hrms_backend.dto.auth;
import com.hrms.hrms_backend.constants.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {


    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;


    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;


    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String firstName;


    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String lastName;


    @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be a valid 10-digit number")
    private String phone;


    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;


    @NotNull(message = "Date of joining is required")
    private LocalDate dateOfJoining;


    @Size(max = 100, message = "Department must not exceed 100 characters")
    private String department;


    @Size(max = 100, message = "Designation must not exceed 100 characters")
    private String designation;


    @NotNull(message = "Role is required")
    private Role role;
}
