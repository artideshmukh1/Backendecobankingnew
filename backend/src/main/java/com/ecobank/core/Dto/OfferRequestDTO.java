package com.ecobank.core.Dto;

import com.ecobank.core.Enums.CustomerSegment;
import com.ecobank.core.Enums.OfferCategoryType;
import com.ecobank.core.Enums.OfferStatus;
import com.ecobank.core.Enums.RedemptionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class OfferRequestDTO {

    private String externalOfferId;
    private String title;
    private String description;
    private OfferCategoryType category;
    private CustomerSegment targetSegment;
    private String eligibilityRules;
    private LocalDate startDate;
    private LocalDate endDate;
    private OfferStatus status;
    private RedemptionType redemptionType;
    private String terms;
    private BigDecimal cashbackAmount;
}
