package com.ecobank.core.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class PartnerSettlementReportDTO {

    @NotBlank
    private String batchId;

    @NotNull
    private LocalDate settlementDate;

    @NotNull
    private BigDecimal totalAmount;

    @NotEmpty
    private List<SettlementItemDTO> items;
}

