package com.ecobank.core.services;

import com.ecobank.core.Dto.RedemptionDTO;
import com.ecobank.core.Enums.RedemptionStatus;
import com.ecobank.core.Enums.WalletState;
import com.ecobank.core.Repository.CustomerRepository;
import com.ecobank.core.Repository.OfferRepository;
import com.ecobank.core.Repository.RedemptionRepository;
import com.ecobank.core.models.Customer;
import com.ecobank.core.models.Offer;
import com.ecobank.core.models.Redemption;
import com.ecobank.core.models.WalletEntry;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.ecobank.core.Repository.WalletEntryRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RedemptionService {
    private final RedemptionRepository redemptionRepository;
    private final CustomerRepository customerRepository;
    private final OfferRepository offerRepository;
    private final WalletEntryRepository walletEntryRepository;

    public RedemptionService(RedemptionRepository redemptionRepository, CustomerRepository customerRepository, OfferRepository offerRepository, WalletEntryRepository walletEntryRepository) {
        this.redemptionRepository = redemptionRepository;
        this.customerRepository = customerRepository;
        this.offerRepository = offerRepository;
        this.walletEntryRepository = walletEntryRepository;
    }

    @Transactional
    public Redemption createRedemption(Long customerId, Long offerId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new IllegalArgumentException("Offer not found"));

        Redemption redemption = new Redemption();
        redemption.setCustomer(customer);
        redemption.setOffer(offer);
        redemption.setStatus(RedemptionStatus.ACTIVATED);
        redemption.setCode(UUID.randomUUID().toString());
        redemption.setCreatedAt(LocalDateTime.now());

        Redemption saved =  redemptionRepository.save(redemption);

        //CREATE WALLET ENTRY
        WalletEntry entry = new WalletEntry();
        entry.setCustomer(customer);                 // IMPORTANT: sets customer_id FK
        entry.setAmount(BigDecimal.valueOf(100));    // example amount
        entry.setCurrency("INR");
        entry.setState(WalletState.CREDITED);
        entry.setCreatedAt(LocalDateTime.now());

        walletEntryRepository.save(entry);

        return saved;
    }

    public RedemptionDTO getRedemption(Long redemptionId) {

        Redemption redemption = redemptionRepository.findById(redemptionId)
                .orElseThrow(() -> new IllegalArgumentException("Redemption not found"));

        RedemptionDTO dto = new RedemptionDTO();
        dto.setRedemptionId(redemption.getRedemptionId());
        dto.setOfferId(redemption.getOffer().getOfferId());
        dto.setStatus(redemption.getStatus().name());
        dto.setCreatedAt(redemption.getCreatedAt());
        dto.setConfirmedAt(redemption.getConfirmedAt());

        return dto;
    }

}
