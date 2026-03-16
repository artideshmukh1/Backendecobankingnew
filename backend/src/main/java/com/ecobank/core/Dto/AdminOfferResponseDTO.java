package com.ecobank.core.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdminOfferResponseDTO {

    private Long offerId;
    private String message;

}

