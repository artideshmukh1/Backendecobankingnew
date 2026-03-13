package com.ecobank.core.controllers;

import com.ecobank.core.Dto.*;
import com.ecobank.core.services.AdminOfferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/admin/offers")
@RequiredArgsConstructor
public class AdminController {

    private final AdminOfferService adminOfferService;

    @PostMapping("/createOrUpdateOffer")
    public ResponseEntity<AdminOfferResponseDTO> createOrUpdateOffer(
            @RequestBody @Valid AdminOfferRequestDTO dto) {

        return ResponseEntity.ok(adminOfferService.createOrUpdate(dto));
    }

    @PostMapping("/onBoardPartner")
    public ResponseEntity<AdminPartnerResponseDTO> onboardPartner(
            @RequestBody @Valid AdminPartnerRequestDTO dto) {

        return ResponseEntity.ok(adminOfferService.onboard(dto));
    }

    @GetMapping("/reconciliation")
    public ResponseEntity<ReconciliationResponseDTO> reconcile(
            @RequestParam("batch_id") String batchId) {

        return ResponseEntity.ok(adminOfferService.reconcile(batchId));
    }
}

