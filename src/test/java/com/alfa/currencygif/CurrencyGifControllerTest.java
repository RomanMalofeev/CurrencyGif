/*
 * Copyright (c) 2021.  Roman Malofeev
 * Junior java developer task for alfa-bank.
 */

package com.alfa.currencygif;

import com.alfa.currencygif.clients.CurrencyExchangeServiceProxy;
import com.alfa.currencygif.clients.GiphyProxy;
import com.alfa.currencygif.models.ExchangeRate;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@AutoConfigureMockMvc
class CurrencyGifControllerTest {

    @MockBean
    private GiphyProxy giphyProxy;
    @MockBean
    private CurrencyExchangeServiceProxy currencyExchangeServiceProxy;
    @Autowired
    private MockMvc mockMvc;

    @Value("${openexchangerates.base}")
    private String base;

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
    String randomGiphy = """
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

    @Test
    void getGif() throws Exception {
        String testUrl = "testUrl";
        Mockito.when(currencyExchangeServiceProxy.Latest(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(this.currentRates);
        Mockito.when(currencyExchangeServiceProxy.Historical(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(this.yesterdayRates);
        Mockito.when(giphyProxy.randomGif(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(randomGiphy);

        mockMvc.perform(MockMvcRequestBuilders.get("/currencies/NG/get-gif"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(testUrl)));
    }

    @Test
    void handleInvalidCurrencyException() throws Exception {
        String currency = "invalid";
        String errorMessage = "Invalid currency: an incorrect currency has been sent, please check the available currencies in the documentation";
        Mockito.when(currencyExchangeServiceProxy.Latest(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(this.currentRates);
        Mockito.when(currencyExchangeServiceProxy.Historical(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(this.yesterdayRates);
        Mockito.when(giphyProxy.randomGif(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(randomGiphy);
        mockMvc.perform(MockMvcRequestBuilders.get("/currencies/"+ currency + "/get-gif"))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(errorMessage)));
    }

    @Test
    void handleJsonProcessingException() throws Exception {
        randomGiphy = "";
        String currency = "EQ";
        String errorMessage = "Internal server error! Please, try again later.";
        Mockito.when(currencyExchangeServiceProxy.Latest(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(this.currentRates);
        Mockito.when(currencyExchangeServiceProxy.Historical(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(this.yesterdayRates);
        Mockito.when(giphyProxy.randomGif(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(randomGiphy);
        mockMvc.perform(MockMvcRequestBuilders.get("/currencies/"+ currency + "/get-gif"))
                .andExpect(MockMvcResultMatchers.status().is(500))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(errorMessage)));
    }
}