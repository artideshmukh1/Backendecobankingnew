package com.ecobank.core.models;

import com.ecobank.core.Enums.WalletState;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "wallet_entries")
public class WalletEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long entryId;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "offer_id")
    private Offer offer;

    @OneToOne
    @JoinColumn(name = "redemption_id")
    private Redemption redemption;

    private BigDecimal amount;

    private String currency;

    @Enumerated(EnumType.STRING)
    private WalletState state;

    private String settlementBatchId;

    private LocalDateTime createdAt;
}

