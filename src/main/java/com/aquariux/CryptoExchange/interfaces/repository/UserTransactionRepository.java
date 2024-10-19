package com.aquariux.CryptoExchange.interfaces.repository;

import com.aquariux.CryptoExchange.models.entity.UserTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTransactionRepository extends JpaRepository<UserTransactionEntity, Long> {


    List<UserTransactionEntity> findAllByAccountId(String username);
}
