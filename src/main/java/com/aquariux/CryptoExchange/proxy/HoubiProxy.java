package com.aquariux.CryptoExchange.proxy;

import com.aquariux.CryptoExchange.models.dtos.HoubiTickerBookResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "houbi-api-service", url = "https://api.huobi.pro")  // Remote service URL
public interface HoubiProxy {

    @GetMapping("/market/tickers")
    HoubiTickerBookResponse getTickerInfo();
}
