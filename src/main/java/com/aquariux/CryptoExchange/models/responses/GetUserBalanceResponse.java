package com.aquariux.CryptoExchange.models.responses;

import com.aquariux.CryptoExchange.models.dtos.UserBalanceDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetUserBalanceResponse {

    private List<UserBalanceDTO> response;
    private GenericErrorResponse errorResponse;
}
