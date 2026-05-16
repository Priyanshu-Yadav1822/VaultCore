package com.vaultcore.backend.dto;

import com.vaultcore.backend.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Returned by transaction endpoints.
 * The "icon" and "color" fields map to the JavaFX UI display constants.
 */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class TransactionResponse {
    private Long   id;
    private String type;          // e.g. "ATM_WITHDRAWAL"
    private String description;   // e.g. "UPI to Riya Patel"
    private Double amount;        // negative = debit, positive = credit
    private String status;        // COMPLETED, PROCESSED, etc.
    private Double balanceAfter;
    private String beneficiaryInfo;
    private LocalDateTime createdAt;

    // Convenience fields for the JavaFX frontend
    private String icon;          // emoji icon
    private String uiColor;       // hex color string

    /** Maps entity → response with auto icon/color assignment */
    public static TransactionResponse from(Transaction t) {
        String icon, color;
        switch (t.getType()) {
            case ATM_WITHDRAWAL -> { icon = "💸"; color = "#00ff88"; }
            case CASH_DEPOSIT   -> { icon = "💰"; color = "#0077ff"; }
            case UPI_TRANSFER   -> { icon = "📲"; color = "#00ff88"; }
            case NEFT_TRANSFER  -> { icon = "🏦"; color = "#ffd700"; }
            case IMPS_TRANSFER  -> { icon = "🔄"; color = "#ffd700"; }
            default             -> { icon = "💳"; color = "#00ffcc"; }
        }
        return TransactionResponse.builder()
            .id(t.getId())
            .type(t.getType().name())
            .description(t.getDescription())
            .amount(t.getAmount())
            .status(t.getStatus().name())
            .balanceAfter(t.getBalanceAfter())
            .beneficiaryInfo(t.getBeneficiaryInfo())
            .createdAt(t.getCreatedAt())
            .icon(icon)
            .uiColor(color)
            .build();
    }
}
