package com.aquariux.CryptoExchange.controllers;

import com.aquariux.CryptoExchange.models.responses.GetUserBalanceResponse;
import com.aquariux.CryptoExchange.services.BookKeepingService;
import com.aquariux.CryptoExchange.services.utilities.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Objects;

@Controller
@Slf4j
public class BookKeepingController {

    @Autowired
    private BookKeepingService bookKeepingService;

    @PostMapping("/get/user/balance")
    public ResponseEntity getUserBalance(@RequestHeader String username) {
        log.info("Hit - /get/user/balance");
        GetUserBalanceResponse response = bookKeepingService.getUserBalance(username);
        if (Objects.nonNull(response.getErrorResponse())) {
            return new ResponseEntity(response.getErrorResponse(), HttpUtils.getStatusCode(response.getErrorResponse().getErrorCode()));
        }
        return new ResponseEntity(response.getResponse(), HttpStatus.OK);
    }

}
