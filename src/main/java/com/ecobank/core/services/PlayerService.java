package com.ecobank.core.services;

import com.ecobank.core.Dto.*;
import com.ecobank.core.Enums.OfferCategoryType;
import com.ecobank.core.Enums.OfferStatus;
import com.ecobank.core.Enums.RedemptionStatus;
import com.ecobank.core.Enums.WalletState;
import com.ecobank.core.Repository.OfferRepository;
import com.ecobank.core.Repository.PlayerRepository;
import com.ecobank.core.Repository.RedemptionRepository;
import com.ecobank.core.Repository.WalletEntryRepository;
import com.ecobank.core.models.Offer;
import com.ecobank.core.models.Player;
import com.ecobank.core.models.Redemption;
import com.ecobank.core.models.WalletEntry;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final RedemptionRepository redemptionRepository;
    private final WalletEntryRepository walletEntryRepository;
    private final HmacVerificationService hmacVerificationService;
    private final IdempotencyService idempotencyService;
    private final PlayerRepository playerRepository;
    private final OfferRepository offerRepository;

    @Transactional
    public void processRedemptionConfirmation(
            Long partnerId,
            String signature,
            String idempotencyKey,
            PartnerRedemptionConfirmationDTO dto) {

        // Idempotency check
        if (idempotencyService.isProcessed(idempotencyKey)) {
            return;
        }

        //HMAC verification
        hmacVerificationService.verify(signature, dto, partnerId);

        Redemption redemption = redemptionRepository.findById(dto.getRedemptionId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid redemption"));

        redemption.setStatus(RedemptionStatus.CONFIRMED);
        redemption.setConfirmedAt(dto.getConfirmationTime());

        WalletEntry entry = new WalletEntry();
        entry.setCustomer(redemption.getCustomer());
        entry.setOffer(redemption.getOffer());
        entry.setRedemption(redemption);
        entry.setAmount(dto.getAmount());
        entry.setCurrency(dto.getCurrency());
        entry.setState(WalletState.CREDITED);
        entry.setCreatedAt(LocalDateTime.now());

        walletEntryRepository.save(entry);

        idempotencyService.markProcessed(idempotencyKey);
    }

    @Transactional
    public void upsertOffers(Long playerId, PartnerCatalogRequestDTO request) {

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Partner not found"));

        for (PartnerOfferDTO dto : request.getOffers()) {

            Offer offer = offerRepository
                    .findByExternalOfferIdAndPlayer_PlayerId(dto.getExternalOfferId(), playerId)
                    .orElse(new Offer());

            offer.setPlayer(player);
            offer.setTitle(dto.getTitle());
            offer.setCategory(OfferCategoryType.valueOf(dto.getCategory()));
            offer.setStartDate(dto.getStartDate());
            offer.setEndDate(dto.getEndDate());
            offer.setTerms(dto.getTerms());
            offer.setStatus(OfferStatus.REVIEW);

            offerRepository.save(offer);
        }
    }

    @Transactional
    public void processSettlementReport(
            Long partnerId,
            PartnerSettlementReportDTO dto) {

        for (SettlementItemDTO item : dto.getItems()) {

            WalletEntry walletEntry =
                    walletEntryRepository.findByRedemption_RedemptionId(
                            item.getRedemptionId()
                    ).orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Wallet entry not found for redemption "
                                            + item.getRedemptionId()
                            )
                    );

            walletEntry.setSettlementBatchId(dto.getBatchId());
            walletEntry.setState(WalletState.SETTLED);
        }
    }



    public PlayerRequestDTO createPlayer(PlayerRequestDTO dto) {

        if (dto.getContactEmail() != null && !dto.getContactEmail().isBlank()) {
            if (playerRepository.existsByContactEmail(dto.getContactEmail())) {
                throw new IllegalArgumentException("Player with this contact email already exists");
            }
        }

        Player player = new Player();
        player.setName(dto.getName());
        player.setKycStatus(dto.getKycStatus());
        player.setTier(dto.getTier());
        player.setContactEmail(dto.getContactEmail());
        player.setContactPhone(dto.getContactPhone());
        player.setHmacSecret(dto.getHmacSecret());

        Player saved = playerRepository.save(player);
        return mapToDTO(saved);
    }


    public PlayerRequestDTO updatePlayer(Long partnerId, PlayerRequestDTO dto) {
        Player player = playerRepository.findById(partnerId)
                .orElseThrow(() -> new RuntimeException("Player not found with id: " + partnerId));

        if (dto.getContactEmail() != null
                && !dto.getContactEmail().isBlank()
                && !dto.getContactEmail().equalsIgnoreCase(player.getContactEmail())) {

            if (playerRepository.existsByContactEmail(dto.getContactEmail())) {
                throw new IllegalArgumentException("Another player with this contact email already exists");
            }
            player.setContactEmail(dto.getContactEmail());
        }

        if (dto.getName() != null && !dto.getName().isBlank()) {
            player.setName(dto.getName());
        }

        if (dto.getKycStatus() != null) {
            player.setKycStatus(dto.getKycStatus());
        }

        if (dto.getTier() != null) {
            player.setTier(dto.getTier());
        }

        if (dto.getContactPhone() != null) {
            player.setContactPhone(dto.getContactPhone());
        }

        if (dto.getHmacSecret() != null && !dto.getHmacSecret().isBlank()) {
            player.setHmacSecret(dto.getHmacSecret());
        }

        Player updated = playerRepository.save(player);
        return mapToDTO(updated);
    }


    public PlayerRequestDTO getPlayerById(Long partnerId) {
        Player player = playerRepository.findById(partnerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found with id: " + partnerId));

        return mapToDTO(player);
    }


    public List<PlayerRequestDTO> getAllPlayers() {
        return playerRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }


    public String deletePlayer(Long partnerId) {
        Player player = playerRepository.findById(partnerId)
                .orElseThrow(() -> new RuntimeException("Player not found with id: " + partnerId));

        if (player.getOffers() != null && !player.getOffers().isEmpty()) {
            throw new IllegalArgumentException("Cannot delete player because offers are linked to this player");
        }

        playerRepository.delete(player);
        return "Player deleted successfully";
    }

    private PlayerRequestDTO mapToDTO(Player player) {
        PlayerRequestDTO dto = new PlayerRequestDTO();
        dto.setPlayerId(player.getPlayerId());
        dto.setName(player.getName());
        dto.setKycStatus(player.getKycStatus());
        dto.setTier(player.getTier());
        dto.setContactEmail(player.getContactEmail());
        dto.setContactPhone(player.getContactPhone());
        dto.setOfferCount(player.getOffers() != null ? player.getOffers().size() : 0);
        return dto;
    }
}

