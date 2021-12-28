/*
 * Copyright (c) 2021.  Roman Malofeev
 * Junior java developer task for alfa-bank.
 */

package com.alfa.currencygif.service;

import com.alfa.currencygif.clients.CurrencyExchangeServiceProxy;
import com.alfa.currencygif.clients.GiphyProxy;
import com.alfa.currencygif.exceptions.InvalidCurrencyException;
import com.alfa.currencygif.models.ExchangeRate;
import com.alfa.currencygif.properties.GiphyProperties;
import com.alfa.currencygif.properties.OpenExchangeRatesProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

@Service
@EnableConfigurationProperties({GiphyProperties.class, OpenExchangeRatesProperties.class})
public class CurrencyGifService {
    private static CurrencyExchangeServiceProxy proxyCurrency ;
    private static GiphyProxy proxyGif;
    private static OpenExchangeRatesProperties openExchangeRatesProperties;
    private static GiphyProperties giphyProperties;

    public CurrencyGifService(CurrencyExchangeServiceProxy proxyCurrency, GiphyProxy proxyGif, OpenExchangeRatesProperties openExchangeRatesProperties, GiphyProperties giphyProperties) {
        CurrencyGifService.proxyCurrency = proxyCurrency;
        CurrencyGifService.proxyGif = proxyGif;
        CurrencyGifService.openExchangeRatesProperties = openExchangeRatesProperties;
        CurrencyGifService.giphyProperties = giphyProperties;
    }

    public static String getDateForHistorical(int period, String dateFormat) {
        GregorianCalendar date = new GregorianCalendar();
        date.add(GregorianCalendar.DATE, -period);
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        return formatter.format(date.getTime());
    }

    public static int compareRates(String currency) throws InvalidCurrencyException {
        String date = getDateForHistorical(openExchangeRatesProperties.getPeriod(), openExchangeRatesProperties.getDateFormat());
        ExchangeRate todayCurrency = proxyCurrency.Latest(openExchangeRatesProperties.getAppId(), openExchangeRatesProperties.getBase(), currency);
        ExchangeRate yesterdayCurrency = proxyCurrency.Historical(openExchangeRatesProperties.getAppId(), date, openExchangeRatesProperties.getBase(), currency);
        if (todayCurrency.getRates().get(currency) == null || yesterdayCurrency.getRates().get(currency) == null) throw new InvalidCurrencyException();
        return Double.compare(todayCurrency.getRates().get(currency), yesterdayCurrency.getRates().get(currency));
    }

    public static String getGifUrlByTag(String tag) throws JsonProcessingException {
        String randomGif = proxyGif.randomGif(giphyProperties.getApiKey(), tag, giphyProperties.getRating());
        return parseUrlFromGiphyJson(randomGif);
    }

    public static String getGifPage(String currency) throws InvalidCurrencyException, JsonProcessingException {
        StringBuilder html = new StringBuilder();
        html.append("<div id=gif align=center>");
        if (compareRates(currency) >= 0) {
            html.append("<img src=\"");
            html.append(getGifUrlByTag(giphyProperties.getPositiveTag()));
            html.append("\" align=\"middle\">");
        } else {
            html.append("<img src=\"");
            html.append(getGifUrlByTag(giphyProperties.getNegativeTag()));
            html.append("\" align=\"middle\">");
        }
        html.append("</div>");
        return html.toString();
    }

    public static String parseUrlFromGiphyJson(String json) throws JsonProcessingException {
            ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(json)
                .get("data")
                .get("images")
                .get("original")
                .get("url").asText();
    }
}
