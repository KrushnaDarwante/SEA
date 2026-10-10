package com.sea.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequest {
    @NotBlank(message = "Username / ID is required")
    private String username;

    private String password;

    private String role; // ADMIN, STUDENT, INVIGILATOR

    private String department;

    private String year;
}
