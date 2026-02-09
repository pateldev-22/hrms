package com.hrms.hrms_backend.dto.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUser {

    private String email;

    private String firstName;

    private String lastName;

    private String phone;

    private LocalDate dateOfBirth;

    private String profilePhotoUrl;

}
