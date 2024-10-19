package com.aquariux.CryptoExchange.proxy;

import com.aquariux.CryptoExchange.models.dtos.BinanceTickerBookDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "binance-api-service", url = "https://api.binance.com/api")  // Remote service URL
public interface BinanceProxy {

    @GetMapping("/v3/ticker/bookTicker")
    List<BinanceTickerBookDTO> getTickerInfo();
}
