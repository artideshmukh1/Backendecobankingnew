package com.ecobank.core.Dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PartnerOfferDTO {

    private String externalOfferId;
    private String title;
    private String category;
    private LocalDate startDate;
    private LocalDate endDate;
    private String redemptionType;
    private String terms;
}

