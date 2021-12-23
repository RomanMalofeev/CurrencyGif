package com.alfa.currencygif.clients;

import com.alfa.currencygif.models.RandomGiphy;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "giphy", url = "${giphy.url}")
public interface GiphyProxy {
    @GetMapping("/random?api_key={api_key}&tag={tag}&rating={rating}")
    RandomGiphy randomGif
            (@PathVariable("api_key") String api_key,
             @PathVariable("tag") String tag,
             @PathVariable("rating") String rating);
}

