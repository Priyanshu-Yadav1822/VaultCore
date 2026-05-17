package com.java_bank.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Returned after successful login — contains JWT + user snapshot for the dashboard */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private String tokenType = "Bearer";

    // User fields the JavaFX dashboard needs immediately
    private Long   userId;
    private String username;
    private String fullName;
    private String initials;
    private String accountLast4;
    private Double currentBalance;
    private Double savingsBalance;
    private Integer creditScore;
    private String role;
    private Boolean active;
}
