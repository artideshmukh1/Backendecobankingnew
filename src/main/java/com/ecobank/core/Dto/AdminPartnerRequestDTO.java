package com.ecobank.core.Dto;

import com.ecobank.core.Enums.KycStatus;
import com.ecobank.core.Enums.PartnerTier;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdminPartnerRequestDTO {

    private Long partnerId; // null = create, not null = update

    @NotBlank
    private String name;

    @NotNull
    private KycStatus kycStatus;

    @NotNull
    private PartnerTier tier;

    private String contactEmail;
    private String contactPhone;
}

