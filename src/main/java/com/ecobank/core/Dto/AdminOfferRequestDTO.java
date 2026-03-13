package com.ecobank.core.Dto;

import com.ecobank.core.Enums.OfferStatus;
import com.ecobank.core.Enums.RedemptionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AdminOfferRequestDTO {

    private Long offerId; // null = create, not null = update

    @NotNull
    private Long partnerId;

    @NotBlank
    private String title;

    private String description;
    private String category;
    private String eligibilityRules;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private OfferStatus status;

    @NotNull
    private RedemptionType redemptionType;

    @NotBlank
    private String externalOfferId;

    private String terms;
}

