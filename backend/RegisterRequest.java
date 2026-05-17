package com.java_bank.backend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/** Used by "Create New Account" button and the admin Create-User panel */
@Data
public class RegisterRequest {

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100)
    private String fullName;

    /** Last 4 digits of the physical card / account number */
    @NotBlank(message = "Account last 4 digits are required")
    @Pattern(regexp = "^[0-9]{4}$", message = "Must be exactly 4 digits")
    private String accountLast4;

    /**
     * Optional — if not provided by the frontend, the backend service
     * should auto-generate from fullName (e.g. "priya.mehta" from "Priya Mehta").
     */
    @Size(min = 3, max = 50)
    @Pattern(regexp = "^[a-zA-Z0-9_.-]+$", message = "Username may only contain letters, digits, _ . -")
    private String username;

    /**
     * Required — user must set their own PIN (4–6 digits).
     */
    @NotBlank(message = "PIN is required")
    @Size(min = 4, max = 6, message = "PIN must be 4–6 digits")
    @Pattern(regexp = "^[0-9]+$", message = "PIN must be numeric")
    private String pin;

    @DecimalMin(value = "0.0", message = "Balance cannot be negative")
    private Double initialCurrentBalance = 0.0;

    @DecimalMin(value = "0.0", message = "Savings cannot be negative")
    private Double initialSavingsBalance = 0.0;

    /** "ROLE_USER" or "ROLE_ADMIN" — defaults to ROLE_USER */
    private String role = "ROLE_USER";
}
