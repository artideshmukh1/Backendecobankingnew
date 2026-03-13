package com.ecobank.core.models;

import com.ecobank.core.Enums.KycStatus;
import com.ecobank.core.Enums.PartnerTier;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "player")
@Data
public class Player {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long playerId;

        @Column(nullable = false)
        private String name;

        @Enumerated(EnumType.STRING)
        private KycStatus kycStatus;

        @Enumerated(EnumType.STRING)
        private PartnerTier tier;

        private String contactEmail;
        private String contactPhone;

        @OneToMany(mappedBy = "player")
        private List<Offer> offers;

        @Column(name = "hmac_secret", nullable = false)
        private String hmacSecret;
    }

