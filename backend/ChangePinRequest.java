package com.vaultcore.backend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ChangePinRequest {

    @NotBlank(message = "Current PIN is required")
    private String currentPin;

    @NotBlank(message = "New PIN is required")
    @Size(min = 4, max = 6, message = "PIN must be 4–6 digits")
    @Pattern(regexp = "^[0-9]+$", message = "PIN must be numeric")
    private String newPin;

    @NotBlank(message = "Please confirm the new PIN")
    private String confirmPin;
}
