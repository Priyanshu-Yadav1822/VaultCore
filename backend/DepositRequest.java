package com.vaultcore.backend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class DepositRequest {

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "1.0", message = "Minimum deposit is ₹1")
    @DecimalMax(value = "1000000.0", message = "Maximum single deposit is ₹10,00,000")
    private Double amount;
}
