package com.aquariux.CryptoExchange.models.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserBalanceResponse {

    private GenericErrorResponse errorResponse;
}
