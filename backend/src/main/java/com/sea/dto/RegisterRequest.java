package com.sea.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String fullName;

    @NotBlank
    private String role; // ADMIN, STUDENT, INVIGILATOR

    // Additional fields for student/teacher
    private String department;
    private Integer year;
    private String phone;
    private String designation;
}
