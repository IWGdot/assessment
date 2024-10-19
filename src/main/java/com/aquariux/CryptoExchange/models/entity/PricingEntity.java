package com.aquariux.CryptoExchange.models.entity;

import com.aquariux.CryptoExchange.models.constants.ExchangeConstants;
import com.aquariux.CryptoExchange.models.dtos.PricingDto;
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

@Entity(name = "tb_pricing_entity")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PricingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String priceUuid;
    private String ticker;
    private String transactionType;
    private BigDecimal price;
    private String provider;
    @CreationTimestamp
    private Timestamp createDt;
    private Timestamp expireDt;

    public PricingEntity(String transactionType, PricingDto dto, String provider) {
        this.priceUuid = UUID.randomUUID().toString();
        this.ticker = dto.getTicker();
        this.transactionType = transactionType;
        this.price = transactionType.equalsIgnoreCase(ExchangeConstants.ASK) ? dto.getAsk() : dto.getBid();
        this.expireDt = dto.getExpireDt();
        this.provider = provider;
    }
}
