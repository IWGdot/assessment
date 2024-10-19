package com.aquariux.CryptoExchange.interfaces.repository;

import com.aquariux.CryptoExchange.models.entity.PricingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public interface PricingRepository extends JpaRepository<PricingEntity, Long> {
    
    Optional<PricingEntity> findAllByTickerAndTransactionTypeAndCreateDtBeforeAndExpireDtAfter(String ticker, String transactionType, Timestamp currentTimestamp, Timestamp currentTimestamp1);

    Optional<PricingEntity> findByPriceUuid(String priceUuid);
}
