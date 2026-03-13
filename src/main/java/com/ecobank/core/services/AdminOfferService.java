package com.ecobank.core.services;

import com.ecobank.core.Dto.*;
import com.ecobank.core.Enums.OfferCategoryType;
import com.ecobank.core.Repository.OfferRepository;
import com.ecobank.core.Repository.PlayerRepository;
import com.ecobank.core.Repository.WalletEntryRepository;
import com.ecobank.core.models.Offer;
import com.ecobank.core.models.Player;
import com.ecobank.core.models.WalletEntry;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminOfferService {

    private final OfferRepository offerRepository;
    private final PlayerRepository playerRepository;
    private final WalletEntryRepository walletEntryRepository;

    @Transactional
    public AdminOfferResponseDTO createOrUpdate(AdminOfferRequestDTO dto) {

        Player partner = playerRepository.findById(dto.getPartnerId())
                .orElseThrow(() -> new IllegalArgumentException("Partner not found"));

        Offer offer = (dto.getOfferId() != null)
                ? offerRepository.findById(dto.getOfferId())
                .orElseThrow(() -> new IllegalArgumentException("Offer not found"))
                : new Offer();

        offer.setPlayer(partner);
        offer.setTitle(dto.getTitle());
        offer.setDescription(dto.getDescription());
        offer.setCategory(OfferCategoryType.valueOf(dto.getCategory()));
        offer.setEligibilityRules(dto.getEligibilityRules());
        offer.setStartDate(dto.getStartDate());
        offer.setEndDate(dto.getEndDate());
        offer.setStatus(dto.getStatus());
        offer.setRedemptionType(dto.getRedemptionType());
        offer.setTerms(dto.getTerms());
        offer.setExternalOfferId(dto.getExternalOfferId());


        offerRepository.save(offer);

        AdminOfferResponseDTO response = new AdminOfferResponseDTO();
        response.setOfferId(offer.getOfferId());
        response.setMessage("Offer saved successfully");

        return response;
    }

    @Transactional
    public AdminPartnerResponseDTO onboard(AdminPartnerRequestDTO dto) {

        Player player = (dto.getPartnerId() != null)
                ? playerRepository.findById(dto.getPartnerId())
                .orElseThrow(() -> new IllegalArgumentException("Partner not found"))
                : new Player();

        player.setName(dto.getName());
        player.setKycStatus(dto.getKycStatus());
        player.setTier(dto.getTier());
        player.setContactEmail(dto.getContactEmail());
        player.setContactPhone(dto.getContactPhone());

        // Generate HMAC secret on first onboarding
        if (player.getPlayerId() == null) {
            player.setHmacSecret(UUID.randomUUID().toString());
        }

        playerRepository.save(player);

        AdminPartnerResponseDTO response = new AdminPartnerResponseDTO();
        response.setPartnerId(player.getPlayerId());
        response.setMessage("Partner onboarded successfully");

        return response;
    }


    public ReconciliationResponseDTO reconcile(String batchId) {

        List<WalletEntry> entries =
                walletEntryRepository.findBySettlementBatchId(batchId);

        BigDecimal totalAmount = entries.stream()
                .map(WalletEntry::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<ReconciliationItemDTO> items = entries.stream()
                .map(entry -> {
                    ReconciliationItemDTO dto = new ReconciliationItemDTO();
                    dto.setWalletEntryId(entry.getEntryId());
                    dto.setRedemptionId(entry.getRedemption().getRedemptionId());
                    dto.setAmount(entry.getAmount());
                    dto.setCurrency(entry.getCurrency());
                    dto.setState(entry.getState());
                    return dto;
                })
                .toList();

        ReconciliationResponseDTO response = new ReconciliationResponseDTO();
        response.setBatchId(batchId);
        response.setTotalAmount(totalAmount);
        response.setTotalTransactions(entries.size());
        response.setItems(items);

        return response;
    }

}

