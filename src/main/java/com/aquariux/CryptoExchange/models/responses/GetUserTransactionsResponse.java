package com.aquariux.CryptoExchange.models.responses;

import com.aquariux.CryptoExchange.models.dtos.UserTransactionDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetUserTransactionsResponse {

    private List<UserTransactionDto> response;
    private GenericErrorResponse errorResponse;
}
