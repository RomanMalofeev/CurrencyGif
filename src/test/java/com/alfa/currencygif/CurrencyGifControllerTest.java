package com.alfa.currencygif;

import com.alfa.currencygif.clients.CurrencyExchangeServiceProxy;
import com.alfa.currencygif.clients.GiphyProxy;
import com.alfa.currencygif.models.DataGiphy;
import com.alfa.currencygif.models.ExchangeRate;
import com.alfa.currencygif.models.ImageGiphy;
import com.alfa.currencygif.models.RandomGiphy;
import org.checkerframework.checker.units.qual.A;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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
    void handleNoSuchElementFoundException() throws Exception {
        String currency = "invalid";
        String errorMasage = "Invalid currency: an incorrect currency has been sent, please check the available currencies in the documentation";
        Mockito.when(currencyExchangeServiceProxy.Latest(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(this.currentRates);
        Mockito.when(currencyExchangeServiceProxy.Historical(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(this.yesterdayRates);
        Mockito.when(giphyProxy.randomGif(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(randomGiphy);
        mockMvc.perform(MockMvcRequestBuilders.get("/currencies/"+ currency + "/get-gif"))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(errorMasage)));
    }
}