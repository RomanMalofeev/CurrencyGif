package com.alfa.currencygif.service;

import com.alfa.currencygif.clients.CurrencyExchangeServiceProxy;
import com.alfa.currencygif.clients.GiphyProxy;
import com.alfa.currencygif.exceptions.InvalidCurrencyException;
import com.alfa.currencygif.models.DataGiphy;
import com.alfa.currencygif.models.ExchangeRate;
import com.alfa.currencygif.models.ImageGiphy;
import com.alfa.currencygif.models.RandomGiphy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CurrencyGifServiceImplTest {

    @Value("${openexchangerates.base}")
    private String base;
    @Autowired
    private CurrencyGifServiceImpl currencyGifService;
    @MockBean
    private GiphyProxy giphyProxy;
    @MockBean
    private CurrencyExchangeServiceProxy currencyExchangeServiceProxy;

    private ExchangeRate currentRates;
    private ExchangeRate yesterdayRates;


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


    @Test
    void compareRatesEquals() throws InvalidCurrencyException {
        Mockito.when(currencyExchangeServiceProxy.Latest(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(this.currentRates);
        Mockito.when(currencyExchangeServiceProxy.Historical(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(this.yesterdayRates);
        int result = currencyGifService.compareRates("EQ");
        Assertions.assertEquals(0, result);
    }

    @Test
    void compareRatesNegative() throws InvalidCurrencyException {
        Mockito.when(currencyExchangeServiceProxy.Latest(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(this.currentRates);
        Mockito.when(currencyExchangeServiceProxy.Historical(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(this.yesterdayRates);
        int result = currencyGifService.compareRates("NG");
        Assertions.assertEquals(-1, result);
    }

    @Test
    void compareRatesPositive() throws InvalidCurrencyException {
        Mockito.when(currencyExchangeServiceProxy.Latest(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(this.currentRates);
        Mockito.when(currencyExchangeServiceProxy.Historical(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(this.yesterdayRates);
        int result = currencyGifService.compareRates("PS");
        Assertions.assertEquals(1, result);
    }

    private RandomGiphy randomGiphy;
    private ImageGiphy imageGiphy;
    private DataGiphy dataGiphy;


    {
        this.imageGiphy = new ImageGiphy();
        this.randomGiphy = new RandomGiphy();
        this.dataGiphy = new DataGiphy();
        imageGiphy.setUrl("testUrl");
        HashMap<String, ImageGiphy> images = new HashMap<>();
        images.put("original", imageGiphy);
        dataGiphy.setImages(images);
        randomGiphy.setData(dataGiphy);
    }
    @Test
    void getPositiveGifUrl() {
        Mockito.when(giphyProxy.randomGif(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(randomGiphy);
        String response = currencyGifService.getPositiveGifUrl();
        assertEquals(response, imageGiphy.getUrl());
    }

    @Test
    void getNegativeGifUrl() {
        Mockito.when(giphyProxy.randomGif(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(randomGiphy);
        String response = currencyGifService.getNegativeGifUrl();
        assertEquals(response, imageGiphy.getUrl());
    }
}