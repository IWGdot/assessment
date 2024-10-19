package com.aquariux.CryptoExchange.services;

import com.aquariux.CryptoExchange.interfaces.repository.UserBalanceRepository;
import com.aquariux.CryptoExchange.models.constants.ErrorCodeConstants;
import com.aquariux.CryptoExchange.models.constants.ExchangeConstants;
import com.aquariux.CryptoExchange.models.dtos.UserBalanceDTO;
import com.aquariux.CryptoExchange.models.entity.UserBalanceEntity;
import com.aquariux.CryptoExchange.models.requests.UpdateUserBalanceRequest;
import com.aquariux.CryptoExchange.models.responses.GenericErrorResponse;
import com.aquariux.CryptoExchange.models.responses.GetUserBalanceResponse;
import com.aquariux.CryptoExchange.models.responses.UpdateUserBalanceResponse;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookKeepingService {

    @Autowired
    private UserBalanceRepository userBalanceRepository;

    @PostConstruct
    public void initUser() {
        List<UserBalanceEntity> userBalances = new ArrayList<>();
        userBalances.add(new UserBalanceEntity("test-user", "USDT", new BigDecimal(50000)));
        userBalances.add(new UserBalanceEntity("test-user", "ETH", new BigDecimal(0)));
        userBalances.add(new UserBalanceEntity("test-user", "BTC", new BigDecimal(0)));
        userBalanceRepository.saveAllAndFlush(userBalances);
        log.info("createdUser");
    }

    public GetUserBalanceResponse getUserBalance(String username) {
        GetUserBalanceResponse response = new GetUserBalanceResponse();
        try {
            List<UserBalanceEntity> userBalances = userBalanceRepository.findByUsername(username);
            List<UserBalanceDTO> userBalanceDTOList = userBalances.stream().map(userBalanceEntity -> new UserBalanceDTO(userBalanceEntity)).collect(Collectors.toList());
            response.setResponse(userBalanceDTOList);
        } catch (Exception e) {
            response.setErrorResponse(new GenericErrorResponse("Unexpected error occurred please contact support", ErrorCodeConstants.UNEXPECTED_ERROR));
        }
        return response;
    }

    public GetUserBalanceResponse getUserTickerBalance(String username, String ticker) {
        GetUserBalanceResponse response = new GetUserBalanceResponse();
        try {
            List<UserBalanceEntity> userBalances = userBalanceRepository.findByUsernameAndTicker(username, ticker);
            List<UserBalanceDTO> userBalanceDTOList = userBalances.stream().map(userBalanceEntity -> new UserBalanceDTO(userBalanceEntity)).collect(Collectors.toList());
            response.setResponse(userBalanceDTOList);
        } catch (Exception e) {
            response.setErrorResponse(new GenericErrorResponse("Unexpected error occurred please contact support", ErrorCodeConstants.UNEXPECTED_ERROR));
        }
        return response;
    }

    @Transactional
    public UpdateUserBalanceResponse updateUserBalance(String username, UpdateUserBalanceRequest updateUserBalanceRequest) {
        UpdateUserBalanceResponse response = new UpdateUserBalanceResponse();
        try {
            List<UserBalanceEntity> userBalance = userBalanceRepository.findByUsernameAndTicker(username, updateUserBalanceRequest.getTicker());
            if (userBalance.isEmpty()) {
                response.setErrorResponse(new GenericErrorResponse("Unable to find balance for ticker", ErrorCodeConstants.BALANCE_NOT_FOUND));
                return response;
            }
            UserBalanceEntity tickerBalance = userBalance.get(0);
            BigDecimal currentBalance = tickerBalance.getBalance();
            switch (updateUserBalanceRequest.getMovementType()) {
                case ExchangeConstants.DELIVERY -> {
                    BigDecimal newBalance = currentBalance.subtract(updateUserBalanceRequest.getAmount());
                    if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                        response.setErrorResponse(new GenericErrorResponse("Insufficient " + tickerBalance.getTicker() + " balance for buy order", ErrorCodeConstants.VALIDATION_ERROR));
                        return response;
                    }
                    tickerBalance.setBalance(newBalance);
                }
                case ExchangeConstants.RECEIVE -> {
                    tickerBalance.setBalance(tickerBalance.getBalance().add(updateUserBalanceRequest.getAmount()));
                }
            }
            userBalanceRepository.saveAndFlush(tickerBalance);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            response.setErrorResponse(new GenericErrorResponse("Unexpected error occurred please contact support", ErrorCodeConstants.UNEXPECTED_ERROR));
        }
        return response;
    }

}
