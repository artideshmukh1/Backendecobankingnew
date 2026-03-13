package com.ecobank.core.Repository;

import com.ecobank.core.Enums.OfferCategoryType;
import com.ecobank.core.Enums.OfferStatus;
import com.ecobank.core.models.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


public interface OfferRepository extends JpaRepository<Offer,Long>{

    Optional<Offer> findByExternalOfferIdAndPlayer_PlayerId(
            String externalOfferId,
            Long playerId
    );

    List<Offer> findByStatusInAndEndDateAfter(
            Collection<OfferStatus> statuses,
            LocalDate date
    );

    List<Offer> findByStatusInAndCategoryAndEndDateAfter(
            Collection<OfferStatus> statuses,
            OfferCategoryType category,
            LocalDate date
    );

    default List<Offer> findActiveOffers(String category, LocalDate date) {

        if (category != null && !category.isBlank()) {
            return findByStatusInAndCategoryAndEndDateAfter(
                    OfferStatus.activeStatuses(),
                    OfferCategoryType.valueOf(category),
                    date
            );
        }
        return findByStatusInAndEndDateAfter(
                OfferStatus.activeStatuses(),
                date
        );
    }
//    List<Offer> findByPlayer_PartnerId(Long partnerId);
List<Offer> findByPlayer_PlayerId(Long partnerId);
    Optional<Offer> findByOfferIdAndPlayer_PlayerId(Long offerId, Long partnerId);

    boolean existsByPlayer_PlayerIdAndExternalOfferId(Long partnerId, String externalOfferId);
}
