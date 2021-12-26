package com.alfa.currencygif.clients;

import com.alfa.currencygif.models.ExchangeRate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "open-exchange-rates", url = "${open-exchange-rates.url}")
public interface CurrencyExchangeServiceProxy {
    @GetMapping("/latest.json?app_id={app_id}&base={base}&symbols={symbols}")
    ExchangeRate Latest
            (@PathVariable("app_id") String app_id,
             @PathVariable("base") String base,
             @PathVariable("symbols") String symbols);

    @GetMapping("/historical/{date}.json?app_id={app_id}&base={base}&symbols={symbols}")
    ExchangeRate Historical
            (@PathVariable("app_id") String app_id,
             @PathVariable("date") String date,
             @PathVariable("base") String base,
             @PathVariable("symbols") String symbols);
}
