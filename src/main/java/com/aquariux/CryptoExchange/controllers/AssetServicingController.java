package com.aquariux.CryptoExchange.controllers;

import com.aquariux.CryptoExchange.models.requests.GetPricingRequest;
import com.aquariux.CryptoExchange.models.requests.TradeRequest;
import com.aquariux.CryptoExchange.models.responses.GetPricingResponse;
import com.aquariux.CryptoExchange.models.responses.GetUserTransactionsResponse;
import com.aquariux.CryptoExchange.models.responses.TradeResponse;
import com.aquariux.CryptoExchange.services.AssetService;
import com.aquariux.CryptoExchange.services.utilities.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Objects;

@Controller
@Slf4j
public class AssetServicingController {

    @Autowired
    private AssetService assetService;

    @PostMapping("/get/user/transactions")
    public ResponseEntity getUserTransactions(@RequestHeader String username) {
        log.info("Hit - /get/user/transactions");
        GetUserTransactionsResponse response = assetService.getUserTransactions(username);
        if (Objects.nonNull(response.getErrorResponse())) {
            return new ResponseEntity(response.getErrorResponse(), HttpUtils.getStatusCode(response.getErrorResponse().getErrorCode()));
        }
        return new ResponseEntity(response.getResponse(), HttpStatus.OK);
    }

    @PostMapping("/get/pricing")
    public ResponseEntity getCurrentPricing(@RequestBody GetPricingRequest request) {
        log.info("Hit - /get/pricing");
        GetPricingResponse response = assetService.getCurrentPricing(request);
        if (Objects.nonNull(response.getErrorResponse())) {
            return new ResponseEntity(response.getErrorResponse(), HttpUtils.getStatusCode(response.getErrorResponse().getErrorCode()));
        }
        return new ResponseEntity(response.getResponse(), HttpStatus.OK);
    }

    @PostMapping("/trade")
    public ResponseEntity trade(@RequestHeader String username, @RequestBody TradeRequest request) {
        log.info("Hit - /trade");
        TradeResponse response = assetService.trade(username, request);
        if (Objects.nonNull(response.getErrorResponse())) {
            return new ResponseEntity(response.getErrorResponse(), HttpUtils.getStatusCode(response.getErrorResponse().getErrorCode()));
        }
        return new ResponseEntity(response.getResponse(), HttpStatus.OK);
    }

    @PostMapping("/trade/marketPrice")
    public ResponseEntity marketPrice(@RequestHeader String username, @RequestBody TradeRequest request) {
        log.info("Hit - /trade");
        TradeResponse response = assetService.trade(username, request);
        if (Objects.nonNull(response.getErrorResponse())) {
            return new ResponseEntity(response.getErrorResponse(), HttpUtils.getStatusCode(response.getErrorResponse().getErrorCode()));
        }
        return new ResponseEntity(response.getResponse(), HttpStatus.OK);
    }

    @Scheduled(fixedDelay = 10000)
    public void priceAggregate() {
        log.info("Updating current best price");
        assetService.updatePriceAggregate();
    }
}
