package com.ecobank.core.models;

import com.ecobank.core.Enums.CustomerSegment;
import com.ecobank.core.Enums.OfferCategoryType;
import com.ecobank.core.Enums.OfferStatus;
import com.ecobank.core.Enums.RedemptionType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "offers", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"player_id", "external_offer_id"})
})

@Data
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long offerId;

    @Column(name = "external_offer_id", nullable = false)
    private String externalOfferId;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    private String title;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    private OfferCategoryType category; // GROCERIES

    @Enumerated(EnumType.STRING)
    private CustomerSegment targetSegment; // PREMIUM-only etc.

    @Column(length = 2000)
    private String eligibilityRules;

    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private OfferStatus status;

    @Enumerated(EnumType.STRING)
    private RedemptionType redemptionType;

    @Column(length = 2000)
    private String terms;

    @Column(precision = 12, scale = 2)
    private BigDecimal cashbackAmount;
}

