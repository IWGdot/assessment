package com.aquariux.CryptoExchange.models.dtos;

import com.aquariux.CryptoExchange.models.constants.ExchangeConstants;
import com.aquariux.CryptoExchange.models.entity.PricingEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PricingDto {

    private String ticker;
    private BigDecimal ask;
    private BigDecimal bid;
    private Timestamp expireDt;
    private String priceUuid;

    public PricingDto(BinanceTickerBookDTO binanceTickerDto, Timestamp expireDt) {
        this.ticker = binanceTickerDto.getSymbol();
        this.ask = new BigDecimal(binanceTickerDto.getAskPrice());
        this.bid = new BigDecimal(binanceTickerDto.getBidPrice());
        this.expireDt = expireDt;
    }

    public PricingDto(HoubiTickerBookDTO houbiTickerDto, Timestamp expireDt) {
        this.ticker = houbiTickerDto.getSymbol().toUpperCase();
        this.ask = houbiTickerDto.getAsk();
        this.bid = houbiTickerDto.getBid();
        this.expireDt = expireDt;
    }

    public PricingDto(PricingEntity entity, String transactionType) {
        this.ticker = entity.getTicker();
        this.ask = transactionType.equalsIgnoreCase(ExchangeConstants.ASK) ? entity.getPrice() : null;
        this.bid = transactionType.equalsIgnoreCase(ExchangeConstants.BID) ? entity.getPrice() : null;
        this.expireDt = entity.getExpireDt();
        this.priceUuid = entity.getPriceUuid();
    }

}
