package com.alfa.currencygif;


import com.alfa.currencygif.exceptions.InvalidCurrencyException;
import com.alfa.currencygif.service.CurrencyGifService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController

public class CurrencyGifController {
    @Autowired
    private CurrencyGifService currencyGifService;

    @RequestMapping("/currencies/{currency}/get-gif")
    public String getGif(@PathVariable String currency) throws InvalidCurrencyException {
        currency = currency.toUpperCase();
        return currencyGifService.getGifPage(currency);
    }

    @ExceptionHandler(InvalidCurrencyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleNoSuchElementFoundException(InvalidCurrencyException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }

}
