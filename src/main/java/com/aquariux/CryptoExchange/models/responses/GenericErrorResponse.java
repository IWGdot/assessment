package com.aquariux.CryptoExchange.models.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GenericErrorResponse {

    private String errorMessage;
    private String errorCode;
}
