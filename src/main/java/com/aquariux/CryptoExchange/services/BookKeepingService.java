package com.aquariux.CryptoExchange.services;

import com.aquariux.CryptoExchange.interfaces.repository.UserBalanceRepository;
import com.aquariux.CryptoExchange.models.constants.ErrorCodeConstants;
import com.aquariux.CryptoExchange.models.dtos.UserBalanceDTO;
import com.aquariux.CryptoExchange.models.entity.UserBalanceEntity;
import com.aquariux.CryptoExchange.models.responses.GenericErrorResponse;
import com.aquariux.CryptoExchange.models.responses.GetUserBalanceResponse;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookKeepingService {

    @Autowired
    private UserBalanceRepository userBalanceRepository;

    @PostConstruct
    public void initUser() {
        UserBalanceEntity newUser = new UserBalanceEntity("test-user", "USDT", new BigDecimal(50000));
        userBalanceRepository.saveAndFlush(newUser);
        log.info("createdUser");
    }

    public GetUserBalanceResponse getUserBalance(String username) {
        GetUserBalanceResponse response = new GetUserBalanceResponse();
        try {
            List<UserBalanceEntity> userBalances = userBalanceRepository.findByUsername(username);
            List<UserBalanceDTO> userBalanceDTOList = userBalances.stream().map(userBalanceEntity -> new UserBalanceDTO(userBalanceEntity)).collect(Collectors.toList());
            response.setResponse(userBalanceDTOList);
        } catch (Exception e) {
            response.setErrorResponse(new GenericErrorResponse("Unexpected error occurred please context support", ErrorCodeConstants.UNEXPECTED_ERROR));
        }
        return response;
    }

}
