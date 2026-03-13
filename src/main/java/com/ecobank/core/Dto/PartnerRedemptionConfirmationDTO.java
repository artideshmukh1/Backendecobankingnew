package com.ecobank.core.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PartnerRedemptionConfirmationDTO {

    @NotNull
    private Long redemptionId;

    @NotNull
    private BigDecimal amount;

    @NotBlank
    private String currency;

    @NotNull
    private LocalDateTime confirmationTime;

    private String externalTransactionId;
}
