package com.aquariux.CryptoExchange.models.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserBalanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String ticker;
    private BigDecimal balance;
    @CreationTimestamp
    private Timestamp createdDt;
    @UpdateTimestamp
    private Timestamp updatedDt;

    public UserBalanceEntity(String username, String ticker, BigDecimal balance) {
        this.username = username;
        this.ticker = ticker;
        this.balance = balance;
    }

}
