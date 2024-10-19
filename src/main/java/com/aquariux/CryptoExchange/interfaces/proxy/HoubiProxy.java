package com.aquariux.CryptoExchange.interfaces.proxy;

import com.aquariux.CryptoExchange.models.dtos.HoubiTickerBookResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "houbi-api-service", url = "https://api.huobi.pro")
public interface HoubiProxy {

    @GetMapping("/market/tickers")
    HoubiTickerBookResponse getTickerInfo();
}
