package com.aquariux.CryptoExchange.models.dtos;

import com.aquariux.CryptoExchange.models.entity.UserBalanceEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserBalanceDTO {

    private String ticker;
    private BigDecimal balance;

    public UserBalanceDTO(UserBalanceEntity entity) {
        this.ticker = entity.getTicker();
        this.balance = entity.getBalance();
    }
}
