package com.ecobank.core.Dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class PartnerCatalogRequestDTO {

    @NotEmpty
    private List<PartnerOfferDTO> offers;
}

