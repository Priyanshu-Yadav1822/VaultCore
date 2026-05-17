package com.java_bank.backend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/** Transfer (UPI / NEFT / IMPS) */
@Data
public class TransferRequest {

    @NotBlank(message = "Beneficiary name is required")
    private String beneficiaryName;

    /** Account number, UPI ID, or mobile number */
    @NotBlank(message = "Beneficiary account / UPI ID is required")
    private String beneficiaryAccount;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "1.0", message = "Minimum transfer is ₹1")
    private Double amount;

    /** UPI_TRANSFER, NEFT_TRANSFER, or IMPS_TRANSFER */
    @NotBlank(message = "Transfer mode is required")
    @Pattern(regexp = "UPI_TRANSFER|NEFT_TRANSFER|IMPS_TRANSFER",
             message = "mode must be UPI_TRANSFER, NEFT_TRANSFER, or IMPS_TRANSFER")
    private String mode;

    @NotBlank(message = "PIN is required")
    private String pin;
}
