package com.alfa.currencygif.service;

import com.alfa.currencygif.clients.CurrencyExchangeServiceProxy;
import com.alfa.currencygif.clients.GiphyProxy;
import com.alfa.currencygif.models.ExchangeBean;
import com.alfa.currencygif.models.RandomGiphy;
import com.alfa.currencygif.properties.GiphyProperties;
import com.alfa.currencygif.properties.OpenExchangeRatesProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

@Service
@EnableConfigurationProperties({GiphyProperties.class, OpenExchangeRatesProperties.class})
public class CurrencyGifServiceImpl implements CurrencyGifService {
    private final CurrencyExchangeServiceProxy proxyCurrency ;
    private final GiphyProxy proxyGif;
    private final OpenExchangeRatesProperties openExchangeRatesProperties;
    private final GiphyProperties giphyProperties;

    public CurrencyGifServiceImpl(CurrencyExchangeServiceProxy proxyCurrency, GiphyProxy proxyGif, OpenExchangeRatesProperties openExchangeRatesProperties, GiphyProperties giphyProperties) {
        this.proxyCurrency = proxyCurrency;
        this.proxyGif = proxyGif;
        this.openExchangeRatesProperties = openExchangeRatesProperties;
        this.giphyProperties = giphyProperties;
    }

    private String getDateForHistorical(int period, String dateFormat) {
        GregorianCalendar date = new GregorianCalendar();
        date.add(GregorianCalendar.DATE, -period);
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        return formatter.format(date.getTime());
    }

    private int compareRates(String currency){
        String date = getDateForHistorical(openExchangeRatesProperties.getPeriod(), openExchangeRatesProperties.getDateFormat());
        ExchangeBean todayCurrency = proxyCurrency.Latest(openExchangeRatesProperties.getAppId(), openExchangeRatesProperties.getBase(), currency);
        ExchangeBean yesturdayCurrency = proxyCurrency.Historical(openExchangeRatesProperties.getAppId(), date, openExchangeRatesProperties.getBase(), currency);
        return Double.compare(todayCurrency.getRates().get(currency), yesturdayCurrency.getRates().get(currency));
    }

    private String getPositiveGifUrl(){
        RandomGiphy randomGif = proxyGif.randomGif(giphyProperties.getApiKey(), giphyProperties.getPositiveTag(), giphyProperties.getRating());
        return randomGif.getData().getImages().get("original").getUrl();
    }

    private String getNegativeGifUrl(){
        RandomGiphy randomGif = proxyGif.randomGif(giphyProperties.getApiKey(), giphyProperties.getNegativeTag(), giphyProperties.getRating());
        return randomGif.getData().getImages().get("original").getUrl();
    }

    @Override
    public String getGifPage(String currency) {
        StringBuffer html = new StringBuffer();
        html.append("<div id=gif align=center>");
        if (compareRates(currency) >= 0) {
            html.append("<img src=\"");
            html.append(getPositiveGifUrl());
            html.append("\" align=\"middle\">");
        } else {
            html.append("<img src=\"");
            html.append(getNegativeGifUrl());
            html.append("\" align=\"middle\">");
        }
        html.append("</div>");
        return html.toString();
    }
}