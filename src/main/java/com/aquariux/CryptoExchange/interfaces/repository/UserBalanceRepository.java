package com.aquariux.CryptoExchange.interfaces.repository;

import com.aquariux.CryptoExchange.models.entity.UserBalanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBalanceRepository extends JpaRepository<UserBalanceEntity, Long> {

    List<UserBalanceEntity> findByUsername(String username);
}
