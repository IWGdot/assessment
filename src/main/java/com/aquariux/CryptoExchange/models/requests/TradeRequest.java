package com.aquariux.CryptoExchange.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TradeRequest {

    private String priceUuid;
    private String transactionType;
    private String ticker;
    private String deliveringTicker;
    private String receivingTicker;
    private String qty;
}
