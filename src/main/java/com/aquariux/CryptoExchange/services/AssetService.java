package com.aquariux.CryptoExchange.services;

import com.aquariux.CryptoExchange.interfaces.repository.PricingRepository;
import com.aquariux.CryptoExchange.interfaces.repository.UserTransactionRepository;
import com.aquariux.CryptoExchange.models.constants.ErrorCodeConstants;
import com.aquariux.CryptoExchange.models.constants.ExchangeConstants;
import com.aquariux.CryptoExchange.models.constants.SupportedTickers;
import com.aquariux.CryptoExchange.models.dtos.BinanceTickerBookDTO;
import com.aquariux.CryptoExchange.models.dtos.HoubiTickerBookResponse;
import com.aquariux.CryptoExchange.models.dtos.PricingDto;
import com.aquariux.CryptoExchange.models.dtos.UserTransactionDto;
import com.aquariux.CryptoExchange.models.entity.PricingEntity;
import com.aquariux.CryptoExchange.models.entity.UserTransactionEntity;
import com.aquariux.CryptoExchange.models.requests.GetPricingRequest;
import com.aquariux.CryptoExchange.models.requests.TradeRequest;
import com.aquariux.CryptoExchange.models.requests.UpdateUserBalanceRequest;
import com.aquariux.CryptoExchange.models.responses.*;
import com.aquariux.CryptoExchange.interfaces.proxy.BinanceProxy;
import com.aquariux.CryptoExchange.interfaces.proxy.HoubiProxy;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AssetService {

    @Autowired
    private UserTransactionRepository userTransactionRepository;

    @Autowired
    private PricingRepository pricingRepository;

    @Autowired
    private BinanceProxy binanceProxy;

    @Autowired
    private HoubiProxy houbiProxy;

    @Autowired
    private BookKeepingService bookKeepingService;

    public GetUserTransactionsResponse getUserTransactions(String username) {
        GetUserTransactionsResponse response = new GetUserTransactionsResponse();
        try {
            List<UserTransactionEntity> transactionEntityList = userTransactionRepository.findAllByAccountId(username);
            List<UserTransactionDto> transactionDtoList = transactionEntityList.stream().map(transactionEntity -> new UserTransactionDto(transactionEntity)).collect(Collectors.toList());
            response.setResponse(transactionDtoList);
        } catch (Exception e) {
            response.setErrorResponse(new GenericErrorResponse("Unexpected error occurred please contact support", ErrorCodeConstants.UNEXPECTED_ERROR));
        }
        return response;
    }

    public GetPricingResponse getCurrentPricing(GetPricingRequest request) {
        GetPricingResponse response = new GetPricingResponse();
        try {
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            Optional<PricingEntity> pricing = pricingRepository.findAllByTickerAndTransactionTypeAndCreateDtBeforeAndExpireDtAfter(request.getTicker(), request.getTransactionType(), currentTimestamp, currentTimestamp);
            if (pricing.isEmpty()) {
                response.setErrorResponse(new GenericErrorResponse("No pricing found for ticker symbol and transaction type", ErrorCodeConstants.PRICING_NOT_FOUND));
                return response;
            }
            response.setResponse(new PricingDto(pricing.get(), request.getTransactionType()));
        } catch (Exception e) {
            response.setErrorResponse(new GenericErrorResponse("Unexpected error occurred please contact support", ErrorCodeConstants.UNEXPECTED_ERROR));
        }
        return response;
    }

    @Transactional
    public TradeResponse trade(String username, TradeRequest request) {
        //Ask price is to let customer buy
        //Bid price is to let customer sell
        TradeResponse response = new TradeResponse();
        String buyOrSell;
        try {
            Optional<PricingEntity> optionalPricing;
            if (StringUtils.isEmpty(request.getPriceUuid())) {
                Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
                optionalPricing = pricingRepository.findAllByTickerAndTransactionTypeAndCreateDtBeforeAndExpireDtAfter(request.getTicker(), request.getTransactionType(), currentTimestamp, currentTimestamp);
            } else {
                optionalPricing = pricingRepository.findByPriceUuid(request.getPriceUuid());
            }
            if (optionalPricing.isEmpty()) {
                response.setErrorResponse(new GenericErrorResponse("No pricing found, please retry", ErrorCodeConstants.VALIDATION_ERROR));
                return response;
            }

            PricingEntity pricing = optionalPricing.get();
            if (pricing.getExpireDt().before(new Timestamp(System.currentTimeMillis()))) {
                response.setErrorResponse(new GenericErrorResponse("Pricing has expired", ErrorCodeConstants.VALIDATION_ERROR));
                return response;
            }
            GetUserBalanceResponse getUserBalanceResponse = bookKeepingService.getUserTickerBalance(username, request.getDeliveringTicker());
            if (Objects.nonNull(getUserBalanceResponse.getErrorResponse())) {
                response.setErrorResponse(getUserBalanceResponse.getErrorResponse());
                return response;
            }

            //Looking for ask price, customer wants to buy
            BigDecimal costOfTrade = pricing.getPrice().multiply(new BigDecimal(request.getQty()));
            if (pricing.getTransactionType().equalsIgnoreCase(ExchangeConstants.ASK)) {
                buyOrSell = ExchangeConstants.BUY;
                if (costOfTrade.compareTo(getUserBalanceResponse.getResponse().get(0).getBalance()) > 0) {
                    response.setErrorResponse(new GenericErrorResponse("Insufficient " + getUserBalanceResponse.getResponse().get(0).getTicker() + " balance for buy order", ErrorCodeConstants.VALIDATION_ERROR));
                    return response;
                }
                bookKeepingService.updateUserBalance(username, new UpdateUserBalanceRequest(request.getDeliveringTicker(), costOfTrade, ExchangeConstants.DELIVERY));
                //InsertMarketMaker Endpoint here//
                bookKeepingService.updateUserBalance(username, new UpdateUserBalanceRequest(request.getReceivingTicker(), new BigDecimal(request.getQty()), ExchangeConstants.RECEIVE));
            }

            //Looking for bid price, customer wants to sell
            else if (pricing.getTransactionType().equalsIgnoreCase(ExchangeConstants.BID)) {
                buyOrSell = ExchangeConstants.SELL;
                if (new BigDecimal(request.getQty()).compareTo(getUserBalanceResponse.getResponse().get(0).getBalance()) > 0) {
                    response.setErrorResponse(new GenericErrorResponse("Insufficient " + getUserBalanceResponse.getResponse().get(0).getTicker() + " balance for sell order", ErrorCodeConstants.VALIDATION_ERROR));
                    return response;
                }
                bookKeepingService.updateUserBalance(username, new UpdateUserBalanceRequest(request.getDeliveringTicker(), new BigDecimal(request.getQty()), ExchangeConstants.DELIVERY));
                //InsertMarketMaker Endpoint here//
                bookKeepingService.updateUserBalance(username, new UpdateUserBalanceRequest(request.getReceivingTicker(), costOfTrade, ExchangeConstants.RECEIVE));
            } else {
                response.setErrorResponse(new GenericErrorResponse("Transaction type not supported", ErrorCodeConstants.VALIDATION_ERROR));
                return response;
            }

            UserTransactionEntity transactionEntity = new UserTransactionEntity(username, request, buyOrSell, pricing);
            userTransactionRepository.saveAndFlush(transactionEntity);
            response.setResponse(new UserTransactionDto(transactionEntity));

        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            response.setErrorResponse(new GenericErrorResponse("Unexpected error occurred please contact support", ErrorCodeConstants.UNEXPECTED_ERROR));
        }
        return response;
    }

    public void updatePriceAggregate() {
        try {
            Timestamp priceExpiry = new Timestamp(System.currentTimeMillis()+10000);
            List<BinanceTickerBookDTO> binancePrices = binanceProxy.getTickerInfo();
            HoubiTickerBookResponse houbiPrices = houbiProxy.getTickerInfo();

            Map<String, PricingDto> bp1 = binancePrices.stream()
                    .filter(bt -> SupportedTickers.TICKER_LIST.contains(bt.getSymbol()))
                    .map(bo -> new PricingDto(bo, priceExpiry))
                    .collect(Collectors.toMap(pricingDto -> pricingDto.getTicker(), pricingDto -> pricingDto));

            Map<String, PricingDto> hp1 = houbiPrices.getData().stream()
                    .filter(ht -> SupportedTickers.TICKER_LIST.contains(ht.getSymbol().toUpperCase()))
                    .map(ho -> new PricingDto(ho, priceExpiry))
                    .collect(Collectors.toMap(pricingDto -> pricingDto.getTicker(), pricingDto -> pricingDto));

            List<PricingEntity> bestPricing = new ArrayList<>();
            for (String ticker : SupportedTickers.TICKER_LIST) {
                //Lower ask price is better for customer who is buying
                if (bp1.get(ticker).getAsk().compareTo(hp1.get(ticker).getAsk()) > 0) {
                    bestPricing.add(new PricingEntity(ExchangeConstants.ASK, hp1.get(ticker), ExchangeConstants.HOUBI));
                } else {
                    bestPricing.add(new PricingEntity(ExchangeConstants.ASK, bp1.get(ticker), ExchangeConstants.BINANCE));
                }
                //Higher bid price is better for customer who is selling
                if (bp1.get(ticker).getBid().compareTo(hp1.get(ticker).getBid()) > 0) {
                    bestPricing.add(new PricingEntity(ExchangeConstants.BID, bp1.get(ticker), ExchangeConstants.BINANCE));
                } else {
                    bestPricing.add(new PricingEntity(ExchangeConstants.BID, hp1.get(ticker), ExchangeConstants.HOUBI));
                }
            }
            //I am purposely not deleting old pricing as we may require old pricing in case of dispute
            pricingRepository.saveAllAndFlush(bestPricing);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
