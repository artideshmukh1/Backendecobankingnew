package com.ecobank.core.Repository;

import com.ecobank.core.Enums.WalletState;
import com.ecobank.core.models.Customer;
import com.ecobank.core.models.WalletEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface WalletEntryRepository extends JpaRepository<WalletEntry, Long> {

    List<WalletEntry> findByCustomer_Id(Long customerId);

    List<WalletEntry> findByState(WalletState state);

    Optional<WalletEntry> findByRedemption_RedemptionId(Long redemptionId);

    List<WalletEntry> findBySettlementBatchId(String settlementBatchId);


}
