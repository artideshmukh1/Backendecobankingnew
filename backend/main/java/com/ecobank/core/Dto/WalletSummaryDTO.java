package com.ecobank.core.Dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletSummaryDTO {
        private BigDecimal balance;
        private String currency;
}
