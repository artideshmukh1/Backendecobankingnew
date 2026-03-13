package com.ecobank.core.Dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SettlementItemDTO {

    private Long redemptionId;
    private BigDecimal amount;
    private String currency;
}
