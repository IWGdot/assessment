package com.aquariux.CryptoExchange.models.responses;

import com.aquariux.CryptoExchange.models.dtos.UserTransactionDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TradeResponse {
    private UserTransactionDto response;
    private GenericErrorResponse errorResponse;
}
