package com.ecobank.core.Dto;

import com.ecobank.core.Enums.KycStatus;
import com.ecobank.core.Enums.PartnerTier;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PlayerRequestDTO {
    private Long playerId;
    @NotBlank(message = "Name is required")
    private String name;

    private KycStatus kycStatus;
    private PartnerTier tier;
    private String contactEmail;
    private String contactPhone;

    @NotBlank(message = "hmacSecret is required")
    private String hmacSecret;
    private int offerCount;
}
