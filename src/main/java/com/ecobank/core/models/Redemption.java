package com.ecobank.core.models;

import com.ecobank.core.Enums.RedemptionStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "redemptions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"customer_id", "offer_id"}))
public class Redemption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long redemptionId;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "offer_id")
    private Offer offer;

    private String code; // promo / clo reference

    @Enumerated(EnumType.STRING)
    private RedemptionStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime confirmedAt;

    @OneToOne(mappedBy = "redemption")
    private WalletEntry walletEntry;
}

