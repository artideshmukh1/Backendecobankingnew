package com.ecobank.core.Dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ReconciliationResponseDTO {

    private String batchId;
    private BigDecimal totalAmount;
    private int totalTransactions;
    private List<ReconciliationItemDTO> items;
}
