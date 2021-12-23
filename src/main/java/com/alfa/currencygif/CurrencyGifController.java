package com.alfa.currencygif;


import com.alfa.currencygif.service.CurrencyGifService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class CurrencyGifController {
    @Autowired
    private CurrencyGifService currencyGifService;

    @RequestMapping("/currencies/{currency}/get-gif")
    public String getGif(@PathVariable String currency){
        return currencyGifService.getGifPage(currency);
    }
}
