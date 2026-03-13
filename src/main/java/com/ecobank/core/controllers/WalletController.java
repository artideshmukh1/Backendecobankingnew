package com.ecobank.core.controllers;

import com.ecobank.core.Dto.WalletEntryDTO;
import com.ecobank.core.Dto.WalletSummaryDTO;
import com.ecobank.core.services.WalletService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/v1/wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping
    public WalletSummaryDTO getWallet(@RequestParam Long customerId) {
        return walletService.getWalletSummary(customerId);
    }

    @GetMapping("/entries")
    public List<WalletEntryDTO> getWalletEntries(@RequestParam Long customerId) {
        return walletService.getWalletEntries(customerId);
    }
}
