package com.ecobank.core.Dto;

import com.ecobank.core.Enums.WalletState;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReconciliationItemDTO {

    private Long walletEntryId;
    private Long redemptionId;
    private BigDecimal amount;
    private String currency;
    private WalletState state;
}

