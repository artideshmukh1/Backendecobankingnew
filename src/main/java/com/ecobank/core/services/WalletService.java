package com.ecobank.core.services;

import com.ecobank.core.Dto.WalletEntryDTO;
import com.ecobank.core.Dto.WalletSummaryDTO;
import com.ecobank.core.Enums.WalletState;
import com.ecobank.core.Repository.WalletEntryRepository;
import com.ecobank.core.models.WalletEntry;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WalletService {

    private final WalletEntryRepository walletEntryRepository;

    public WalletService(WalletEntryRepository walletEntryRepository) {
        this.walletEntryRepository = walletEntryRepository;
    }

    public WalletSummaryDTO getWalletSummary(Long customerId) {

        BigDecimal balance = calculateBalance(customerId);

        WalletSummaryDTO dto = new WalletSummaryDTO();
        dto.setBalance(balance);
        dto.setCurrency("INR");

        return dto;
    }

    public List<WalletEntryDTO> getWalletEntries(Long customerId) {

        return walletEntryRepository.findByCustomer_Id(customerId)
                .stream()
                .map(entry -> {
                    WalletEntryDTO dto = new WalletEntryDTO();
                    dto.setEntryId(entry.getEntryId());
                    dto.setAmount(entry.getAmount());
                    dto.setCurrency(entry.getCurrency());
                    dto.setState(entry.getState().name());
                    dto.setCreatedAt(entry.getCreatedAt());
                    return dto;
                })
                .toList();
    }

    private BigDecimal calculateBalance(Long customerId) {

        List<WalletEntry> entries =
                walletEntryRepository.findByCustomer_Id(customerId);

        BigDecimal balance = BigDecimal.ZERO;

        for (WalletEntry entry : entries) {

            if (entry.getState() == WalletState.CREDITED ||
                    entry.getState() == WalletState.SETTLED) {

                balance = balance.add(entry.getAmount());

            } else if (entry.getState() == WalletState.REVERSED) {

                balance = balance.subtract(entry.getAmount());
            }
            // PENDING → do nothing
        }

        return balance;
    }

}
