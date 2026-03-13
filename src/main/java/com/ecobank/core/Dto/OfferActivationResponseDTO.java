package com.ecobank.core.Dto;

import lombok.Data;

@Data
public class OfferActivationResponseDTO {

    private Long redemptionId;
    private String status;
    private String code;
}

