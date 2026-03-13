package com.ecobank.core.Dto;

import com.ecobank.core.Enums.CustomerSegment;
import com.ecobank.core.Enums.OfferStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class OfferDTO {
    private Long offerId;
    private String title;
    private String category;
    private String partnerName;
    private String redemptionType;
    private LocalDate validTill;

    private String externalOfferId;
    private Long playerId;
    private String description;
    private CustomerSegment targetSegment;
    private String eligibilityRules;
    private LocalDate startDate;
    private LocalDate endDate;
    private OfferStatus status;
    private String terms;
    private BigDecimal cashbackAmount;
}
