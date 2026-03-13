package com.ecobank.core.Repository;

import com.ecobank.core.Enums.RedemptionStatus;
import com.ecobank.core.models.Redemption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RedemptionRepository extends JpaRepository<Redemption,Long> {

    Optional<Redemption> findByCustomer_IdAndOffer_OfferId(
            Long customerId,
            Long offerId
    );

    List<Redemption> findByStatus(RedemptionStatus status);
}
