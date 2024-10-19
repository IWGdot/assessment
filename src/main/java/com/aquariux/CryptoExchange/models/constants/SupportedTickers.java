package com.aquariux.CryptoExchange.models.constants;


import java.util.ArrayList;
import java.util.List;

public class SupportedTickers {

    public static String ETHUSDT = "ETHUSDT";
    public static String BTCUSDT = "BTCUSDT";

    public static final List<String> TICKER_LIST;
    static {
        List<String> tickerList = new ArrayList<>();
        tickerList.add(ETHUSDT);
        tickerList.add(BTCUSDT);
        TICKER_LIST = tickerList;
    }
}
