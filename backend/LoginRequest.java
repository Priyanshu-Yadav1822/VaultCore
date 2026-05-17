package com.java_bank.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/** Sent by the JavaFX login form (Account ID + PIN) */
@Data
public class LoginRequest {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "PIN is required")
    @Size(min = 4, max = 60, message = "PIN must be at least 4 characters")
    private String pin;
}
