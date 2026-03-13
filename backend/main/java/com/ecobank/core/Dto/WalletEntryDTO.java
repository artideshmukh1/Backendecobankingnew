package com.ecobank.core.Dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class WalletEntryDTO {
    private Long entryId;
    private BigDecimal amount;
    private String currency;
    private String state;
    private LocalDateTime createdAt;
}
