/*
 * Copyright (c) 2021.  Roman Malofeev
 * Junior java developer task for alfa-bank.
 */
package com.alfa.currencygif;


import com.alfa.currencygif.exceptions.InvalidCurrencyException;
import com.alfa.currencygif.service.CurrencyGifService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController

public class CurrencyGifController {
    private final CurrencyGifService currencyGifService;

    public CurrencyGifController(CurrencyGifService currencyGifService) {
        this.currencyGifService = currencyGifService;
    }

    @RequestMapping("/currencies/{currency}/get-gif")
    public String getGif(@PathVariable String currency) throws InvalidCurrencyException, JsonProcessingException {
        currency = currency.toUpperCase();
        return currencyGifService.getGifPage(currency);
    }

    @ExceptionHandler(InvalidCurrencyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleInvalidCurrencyException(InvalidCurrencyException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }

    @ExceptionHandler({JsonProcessingException.class, NullPointerException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleJsonProcessingException() {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal server error! Please, try again later.");
    }

}
