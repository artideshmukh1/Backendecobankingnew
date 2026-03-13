package com.ecobank.core.services;

import com.ecobank.core.models.Customer;
import com.ecobank.core.models.Offer;
import org.springframework.stereotype.Service;

@Service
public interface EligibilityService {
    boolean isEligible(Customer customer, Offer offer);
}
