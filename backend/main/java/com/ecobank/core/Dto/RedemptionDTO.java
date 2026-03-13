package com.ecobank.core.Dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RedemptionDTO {

    private Long redemptionId;
    private Long offerId;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime confirmedAt;
}
