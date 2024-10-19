package com.aquariux.CryptoExchange.services.utilities;

import ch.qos.logback.core.util.StringUtil;
import com.aquariux.CryptoExchange.models.constants.ErrorCodeConstants;
import org.springframework.http.HttpStatus;

public class HttpUtils {

    public static HttpStatus getStatusCode(String errorCode) {
        if(StringUtil.isNullOrEmpty(errorCode)) return HttpStatus.INTERNAL_SERVER_ERROR;
        if (errorCode.equalsIgnoreCase(ErrorCodeConstants.UNEXPECTED_ERROR)) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
