package com.java_bank.backend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/** Withdraw — requires PIN for authorization */
@Data
public class WithdrawRequest {

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "1.0", message = "Minimum withdrawal is ₹1")
    @DecimalMax(value = "100000.0", message = "Maximum single withdrawal is ₹1,00,000")
    private Double amount;

    @NotBlank(message = "PIN is required")
    private String pin;
}
