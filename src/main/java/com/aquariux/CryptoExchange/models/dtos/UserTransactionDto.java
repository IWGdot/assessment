package com.aquariux.CryptoExchange.models.dtos;

import com.aquariux.CryptoExchange.models.entity.UserTransactionEntity;
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
public class UserTransactionDto {
    private String accountId;
    private String transactionId;
    private String transactionType;
    private String ticker;
    private BigDecimal price;
    private BigDecimal qty;
    //May have cases where system admin might need to move funds etc. E.g. staking rewards
    private String createUserId;
    //Incases where transferring to/from chain this status would be required, although this is not currently asked from the task
    private String transactionStatus;
    private Timestamp createDt;
    //Incases where transferring to/from chain this status would be required, although this is not currently asked from the task
    private Timestamp settlementDt;

    public UserTransactionDto(UserTransactionEntity entity) {
        this.accountId = entity.getAccountId();
        this.transactionId = entity.getTransactionId();
        this.transactionType = entity.getTransactionType();
        this.ticker = entity.getTicker();
        this.price = entity.getPrice();
        this.qty = entity.getQty();
        this.createUserId = entity.getCreateUserId();
        this.transactionStatus = entity.getTransactionStatus();
        this.createDt = entity.getCreateDt();
        this.settlementDt = entity.getSettlementDt();
    }
}
