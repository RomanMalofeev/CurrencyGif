/*
 * Copyright (c) 2021.  Roman Malofeev
 * Junior java developer task for alfa-bank.
 */

package com.alfa.currencygif.service;

import com.alfa.currencygif.clients.CurrencyExchangeServiceProxy;
import com.alfa.currencygif.clients.GiphyProxy;
import com.alfa.currencygif.exceptions.InvalidCurrencyException;
import com.alfa.currencygif.models.ExchangeRate;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CurrencyGifServiceTest {

    @Value("${openexchangerates.base}")
    private String base;
    @Value("${giphy.positive-tag}")
    private String positiveGifTag;
    @Value("${giphy.negative-tag}")
    private String negativeGifTag;
    @MockBean
    private GiphyProxy giphyProxy;
    @MockBean
    private CurrencyExchangeServiceProxy currencyExchangeServiceProxy;

    private final ExchangeRate currentRates;
    private final ExchangeRate yesterdayRates;


    {
        int time = 1634739688;
        this.currentRates = new ExchangeRate();
        this.currentRates.setTimestamp(time);
        this.currentRates.setBase(this.base);
        Map<String, Double> currentRatesMap = new HashMap<>();
        currentRatesMap.put("EQ", 0.3);
        currentRatesMap.put("NG", 0.7);
        currentRatesMap.put("PS", 3.0);
        this.currentRates.setRates(currentRatesMap);

        time = 1634653288;
        this.yesterdayRates = new ExchangeRate();
        this.yesterdayRates.setTimestamp(time);
        this.yesterdayRates.setBase(this.base);
        Map<String, Double> yesterdayRatesMap = new HashMap<>();
        yesterdayRatesMap.put("EQ", 0.3);
        yesterdayRatesMap.put("NG", 0.9);
        yesterdayRatesMap.put("PS", 2.8);
        this.yesterdayRates.setRates(yesterdayRatesMap);
    }

    final String randomGiphy = """
            {
              "data": {
                "images": {
                  "original": {
                    "url": "testUrl"
                  }
                }
              },
              "meta": {
                "msg": "OK",
                "status": 200
              }
            }""";
    final String testUrlGif = "testUrl";


    @Test
    void compareRatesEquals() throws InvalidCurrencyException {
        Mockito.when(currencyExchangeServiceProxy.Latest(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(this.currentRates);
        Mockito.when(currencyExchangeServiceProxy.Historical(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(this.yesterdayRates);
        int result = CurrencyGifService.compareRates("EQ");
        assertEquals(0, result);
    }

    @Test
    void compareRatesNegative() throws InvalidCurrencyException {
        Mockito.when(currencyExchangeServiceProxy.Latest(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(this.currentRates);
        Mockito.when(currencyExchangeServiceProxy.Historical(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(this.yesterdayRates);
        int result = CurrencyGifService.compareRates("NG");
        assertEquals(-1, result);
    }

    @Test
    void compareRatesPositive() throws InvalidCurrencyException {
        Mockito.when(currencyExchangeServiceProxy.Latest(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(this.currentRates);
        Mockito.when(currencyExchangeServiceProxy.Historical(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(this.yesterdayRates);
        int result = CurrencyGifService.compareRates("PS");
        assertEquals(1, result);
    }
    @Test
    void getPositiveGifUrl() throws JsonProcessingException {
        Mockito.when(giphyProxy.randomGif(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(randomGiphy);
        String response = CurrencyGifService.getGifUrlByTag(positiveGifTag);
        assertEquals(response, testUrlGif);
    }

    @Test
    void getNegativeGifUrl() throws JsonProcessingException {
        Mockito.when(giphyProxy.randomGif(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(randomGiphy);
        String response = CurrencyGifService.getGifUrlByTag(negativeGifTag);
        assertEquals(response, testUrlGif);
    }
}