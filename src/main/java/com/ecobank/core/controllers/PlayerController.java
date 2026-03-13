package com.ecobank.core.controllers;

import com.ecobank.core.Dto.PartnerCatalogRequestDTO;
import com.ecobank.core.Dto.PartnerRedemptionConfirmationDTO;
import com.ecobank.core.Dto.PartnerSettlementReportDTO;
import com.ecobank.core.Dto.PlayerRequestDTO;
import com.ecobank.core.services.PlayerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
//@RequestMapping("/v1/partners/{partnerId}")
@RequestMapping("/v1/partners")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PLAYER')")
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping("/player-dashboard")
    public ResponseEntity<?> game() {
        return ResponseEntity.ok(Map.of("message", "Welcome player!"));
    }

    // Redemption confirmation webhook
    @PostMapping("/webhooks/redemption-confirmed")
    public ResponseEntity<Void> redemptionConfirmed(
            @PathVariable Long partnerId,
            @RequestHeader("X-Signature") String signature,
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @RequestBody @Valid PartnerRedemptionConfirmationDTO dto) {

        playerService.processRedemptionConfirmation(
                partnerId, signature, idempotencyKey, dto);
        return ResponseEntity.ok().build();
    }

    // Offer catalog batch upsert
    @PostMapping("/catalog")
    public ResponseEntity<Void> uploadCatalog(
            @PathVariable Long partnerId,
            @RequestBody @Valid PartnerCatalogRequestDTO request) {

        playerService.upsertOffers(partnerId, request);
        return ResponseEntity.ok().build();
    }

    // Settlement report
    @PostMapping("/settlement-report")
    public ResponseEntity<Void> uploadSettlementReport(
            @PathVariable Long partnerId,
            @RequestBody @Valid PartnerSettlementReportDTO dto) {

        playerService.processSettlementReport(partnerId, dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create")
    public ResponseEntity<PlayerRequestDTO> createPlayer(@Valid @RequestBody PlayerRequestDTO dto) {
        return ResponseEntity.ok(playerService.createPlayer(dto));
    }

    @PutMapping("/{partnerId}")
    public ResponseEntity<PlayerRequestDTO> updatePlayer(@PathVariable Long partnerId,
                                                          @RequestBody PlayerRequestDTO dto) {
        return ResponseEntity.ok(playerService.updatePlayer(partnerId, dto));
    }

    @GetMapping("/{partnerId}")
    public ResponseEntity<PlayerRequestDTO> getPlayerById(@PathVariable Long partnerId) {
        return ResponseEntity.ok(playerService.getPlayerById(partnerId));
    }

    @GetMapping("/getAllPlayers")
    public ResponseEntity<List<PlayerRequestDTO>> getAllPlayers() {
        return ResponseEntity.ok(playerService.getAllPlayers());
    }

    @DeleteMapping("/{partnerId}")
    public ResponseEntity<String> deletePlayer(@PathVariable Long partnerId) {
        return ResponseEntity.ok(playerService.deletePlayer(partnerId));
    }
}

