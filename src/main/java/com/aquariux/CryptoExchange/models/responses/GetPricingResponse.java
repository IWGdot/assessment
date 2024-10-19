package com.aquariux.CryptoExchange.models.responses;

import com.aquariux.CryptoExchange.models.dtos.PricingDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetPricingResponse {
    private PricingDto response;
    private GenericErrorResponse errorResponse;
}
