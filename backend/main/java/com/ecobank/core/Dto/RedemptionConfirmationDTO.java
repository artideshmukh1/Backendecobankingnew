package com.ecobank.core.Dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RedemptionConfirmationDTO {

    private Long redemptionId;
    private BigDecimal amount;
    private String currency;
    private LocalDateTime confirmationTime;
}

