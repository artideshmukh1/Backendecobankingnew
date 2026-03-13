package com.ecobank.core.services;

import com.ecobank.core.Enums.CustomerSegment;
import com.ecobank.core.Enums.OfferCategoryType;
import com.ecobank.core.Enums.OfferStatus;
import com.ecobank.core.Repository.RedemptionRepository;
import com.ecobank.core.models.Customer;
import com.ecobank.core.models.Offer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class EligibilityServiceImpl implements EligibilityService {

    private final RedemptionRepository redemptionRepository;

    @Override
    public boolean isEligible(Customer customer, Offer offer) {

        // Offer must be in active lifecycle stage
        if (!OfferStatus.activeStatuses().contains(offer.getStatus())) {
            return false;
        }

        //Offer must be within validity window
        LocalDate today = LocalDate.now();
        if (offer.getStartDate().isAfter(today)
                || offer.getEndDate().isBefore(today)) {
            return false;
        }

        // Customer consent check
        if (customer.getConsents() == null
                || !customer.getConsents().contains("OFFERS")) {
            return false;
        }

        // Premium-only offers should not show to retail customers
//        if (OfferCategory.PREMIUM.equals(offer.getCategory())
//                && !OfferCategory.PREMIUM.equals(customer.getSegment())) {
//            return false;
//        }

        if (offer.getTargetSegment() == CustomerSegment.PREMIUM
                && !customer.getSegment().equals(CustomerSegment.PREMIUM) ) {
            return false;
        }



        // Prevent multiple redemptions of same offer
        boolean alreadyRedeemed =
                redemptionRepository
                        .findByCustomer_IdAndOffer_OfferId(
                                customer.getId(),
                                offer.getOfferId()
                        )

                        .isPresent();

        if (alreadyRedeemed) {
            return false;
        }

        //Eligible
        return true;
    }
}


