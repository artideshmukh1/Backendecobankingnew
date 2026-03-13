package com.ecobank.core.services;

import com.ecobank.core.Dto.OfferActivationResponseDTO;
import com.ecobank.core.Dto.OfferDTO;
import com.ecobank.core.Dto.OfferRequestDTO;
import com.ecobank.core.Enums.OfferStatus;
import com.ecobank.core.Repository.CustomerRepository;
import com.ecobank.core.Repository.OfferRepository;
import com.ecobank.core.Repository.PlayerRepository;
import com.ecobank.core.models.Customer;
import com.ecobank.core.models.Offer;
import com.ecobank.core.models.Player;
import com.ecobank.core.models.Redemption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OfferService {

    private final OfferRepository offerRepository;
    private final CustomerRepository customerRepository;
    private final RedemptionService redemptionService;
    private final EligibilityService eligibilityService;
    private final PlayerRepository playerRepository;

    public List<OfferDTO> getEligibleOffers(Long customerId, String category, int limit) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        List<Offer> offers = offerRepository.findActiveOffers(category, LocalDate.now());

        return offers.stream()
                .filter(offer -> eligibilityService.isEligible(customer, offer))
                .limit(limit)
                .map(this::toDTO)
                .toList();
    }

    public OfferDTO getOfferDetails(Long offerId) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new IllegalArgumentException("Offer not found"));
        return toDTO(offer);
    }

    public OfferActivationResponseDTO activateOffer(Long offerId, Long customerId) {
        Redemption redemption = redemptionService.createRedemption(customerId, offerId);

        OfferActivationResponseDTO dto = new OfferActivationResponseDTO();
        dto.setRedemptionId(redemption.getRedemptionId());
        dto.setStatus(redemption.getStatus().name());
        dto.setCode(redemption.getCode());

        return dto;
    }

    private OfferDTO toDTO(Offer offer) {
        OfferDTO dto = new OfferDTO();
        dto.setOfferId(offer.getOfferId());
        dto.setPlayerId(offer.getPlayer().getPlayerId());
        dto.setTitle(offer.getTitle());
        dto.setCategory(String.valueOf(offer.getCategory()));
        dto.setPartnerName(offer.getPlayer().getName());
        dto.setRedemptionType(offer.getRedemptionType().name());
        dto.setValidTill(offer.getEndDate());
        dto.setExternalOfferId(offer.getExternalOfferId());
        dto.setDescription(offer.getDescription());
        dto.setTargetSegment(offer.getTargetSegment());
        dto.setEligibilityRules(offer.getEligibilityRules());
        dto.setStartDate(offer.getStartDate());
        dto.setEndDate(offer.getEndDate());
        dto.setStatus(offer.getStatus());
        dto.setTerms(offer.getTerms());
        dto.setCashbackAmount(offer.getCashbackAmount());
        return dto;
    }

    public OfferDTO addOffer(Long playerId, OfferRequestDTO dto) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found with id: " + playerId));

        boolean exists = offerRepository.existsByPlayer_PlayerIdAndExternalOfferId(playerId, dto.getExternalOfferId());
        if (exists) {
            throw new RuntimeException("Offer with same externalOfferId already exists for this player.");
        }

        Offer offer = new Offer();
        offer.setExternalOfferId(dto.getExternalOfferId());
        offer.setPlayer(player);
        offer.setTitle(dto.getTitle());
        offer.setDescription(dto.getDescription());
        offer.setCategory(dto.getCategory());
        offer.setTargetSegment(dto.getTargetSegment());
        offer.setEligibilityRules(dto.getEligibilityRules());
        offer.setStartDate(dto.getStartDate());
        offer.setEndDate(dto.getEndDate());
        offer.setStatus(dto.getStatus());
        offer.setRedemptionType(dto.getRedemptionType());
        offer.setTerms(dto.getTerms());
        offer.setCashbackAmount(dto.getCashbackAmount());

        Offer savedOffer = offerRepository.save(offer);
        return toDTO(savedOffer);
    }

    public OfferDTO updateOffer(Long playerId, Long offerId, OfferRequestDTO dto) {
        Offer offer = offerRepository.findByOfferIdAndPlayer_PlayerId(offerId, playerId)
                .orElseThrow(() -> new RuntimeException("Offer not found for given player."));

        offer.setExternalOfferId(dto.getExternalOfferId());
        offer.setTitle(dto.getTitle());
        offer.setDescription(dto.getDescription());
        offer.setCategory(dto.getCategory());
        offer.setTargetSegment(dto.getTargetSegment());
        offer.setEligibilityRules(dto.getEligibilityRules());
        offer.setStartDate(dto.getStartDate());
        offer.setEndDate(dto.getEndDate());
        offer.setStatus(dto.getStatus());
        offer.setRedemptionType(dto.getRedemptionType());
        offer.setTerms(dto.getTerms());
        offer.setCashbackAmount(dto.getCashbackAmount());

        Offer updatedOffer = offerRepository.save(offer);
        return toDTO(updatedOffer);
    }

    public String deleteOffer(Long playerId, Long offerId) {
        Offer offer = offerRepository.findByOfferIdAndPlayer_PlayerId(offerId, playerId)
                .orElseThrow(() -> new RuntimeException("Offer not found for given player."));

        offerRepository.delete(offer);
        return "Offer deleted successfully.";
    }

    public OfferDTO getOfferById(Long playerId, Long offerId) {
        Offer offer = offerRepository.findByOfferIdAndPlayer_PlayerId(offerId, playerId)
                .orElseThrow(() -> new RuntimeException("Offer not found for given player."));

        return toDTO(offer);
    }

    public List<OfferDTO> getAllOffersByPlayer(Long playerId) {
        List<Offer> offers = offerRepository.findByPlayer_PlayerId(playerId);

        return offers.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}

