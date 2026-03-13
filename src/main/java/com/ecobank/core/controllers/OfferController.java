package com.ecobank.core.controllers;

import com.ecobank.core.Dto.OfferActivationResponseDTO;
import com.ecobank.core.Dto.OfferDTO;
import com.ecobank.core.Dto.OfferRequestDTO;
import com.ecobank.core.services.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/offers")
@RequiredArgsConstructor
public class OfferController {

    private final OfferService offerService;

    // customer APIs
    @GetMapping
    public List<OfferDTO> getOffers(
            @RequestParam Long customerId,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "10") int limit) {

        return offerService.getEligibleOffers(customerId, category, limit);
    }

    @GetMapping("/{offerId}")
    public ResponseEntity<?> getOfferDetails(@PathVariable Long offerId) {
        try{
            OfferDTO offerDTO = offerService.getOfferDetails(offerId);
            return ResponseEntity.ok(offerDTO);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to fetch offer details:" + e.getMessage());
        }
    }

    @PostMapping("/{offerId}/activate")
    public OfferActivationResponseDTO activateOffer(
            @PathVariable Long offerId,
            @RequestParam Long customerId) {

        return offerService.activateOffer(offerId, customerId);
    }

    // player/admin APIs
    @PostMapping("/player/{playerId}")
    public ResponseEntity<OfferDTO> addOffer(@PathVariable Long playerId,
                                             @RequestBody OfferRequestDTO dto) {
        return ResponseEntity.ok(offerService.addOffer(playerId, dto));
    }

    @PutMapping("/player/{playerId}/{offerId}")
    public ResponseEntity<OfferDTO> updateOffer(@PathVariable Long playerId,
                                                @PathVariable Long offerId,
                                                @RequestBody OfferRequestDTO dto) {
        return ResponseEntity.ok(offerService.updateOffer(playerId, offerId, dto));
    }

    @DeleteMapping("/player/{playerId}/{offerId}")
    public ResponseEntity<String> deleteOffer(@PathVariable Long playerId,
                                              @PathVariable Long offerId) {
        return ResponseEntity.ok(offerService.deleteOffer(playerId, offerId));
    }

    @GetMapping("/player/{playerId}/{offerId}")
    public ResponseEntity<OfferDTO> getOfferById(@PathVariable Long playerId,
                                                 @PathVariable Long offerId) {
        return ResponseEntity.ok(offerService.getOfferById(playerId, offerId));
    }

    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<OfferDTO>> getAllOffersByPlayer(@PathVariable Long playerId) {
        return ResponseEntity.ok(offerService.getAllOffersByPlayer(playerId));
    }
}