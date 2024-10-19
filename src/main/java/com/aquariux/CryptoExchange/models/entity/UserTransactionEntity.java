package com.aquariux.CryptoExchange.models.entity;

import com.aquariux.CryptoExchange.models.constants.ExchangeConstants;
import com.aquariux.CryptoExchange.models.dtos.PricingDto;
import com.aquariux.CryptoExchange.models.requests.TradeRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Entity(name = "tb_user_transaction")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountId;
    private String transactionId;
    private String transactionType;
    private String ticker;
    private BigDecimal price;
    private BigDecimal qty;
    private String createUserId;
    private String transactionStatus;
    @CreationTimestamp
    private Timestamp createDt;
    private Timestamp settlementDt;

    public UserTransactionEntity(String username, TradeRequest request, String transactionType, PricingEntity pricing) {
        this.accountId = username;
        this.transactionId = UUID.randomUUID().toString();
        this.transactionType = transactionType;
        this.ticker = pricing.getTicker();
        this.price = pricing.getPrice();
        this.qty = new BigDecimal(request.getQty());
        this.createUserId = username;
        this.transactionStatus = ExchangeConstants.SUCCESS;
        this.settlementDt = new Timestamp(System.currentTimeMillis());
    }

}
