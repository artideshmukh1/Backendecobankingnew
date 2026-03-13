package com.ecobank.core.controllers;

import com.ecobank.core.Dto.RedemptionDTO;
import com.ecobank.core.services.RedemptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/redemptions")
public class RedemptionController {

    private final RedemptionService redemptionService;

    public RedemptionController(RedemptionService redemptionService) {
        this.redemptionService = redemptionService;
    }

    @GetMapping("/{redemptionId}")
    public RedemptionDTO getRedemption(@PathVariable Long redemptionId) {
        return redemptionService.getRedemption(redemptionId);
    }
}
