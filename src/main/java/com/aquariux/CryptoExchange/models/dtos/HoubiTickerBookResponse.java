package com.aquariux.CryptoExchange.models.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HoubiTickerBookResponse {

    private List<HoubiTickerBookDTO> data;
}
