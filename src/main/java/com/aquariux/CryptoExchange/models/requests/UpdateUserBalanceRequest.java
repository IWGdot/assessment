package com.aquariux.CryptoExchange.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserBalanceRequest {

    private String ticker;
    private BigDecimal amount;
    private String movementType;

}
