package com.alfa.currencygif.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect
public class ImageGiphy {

    @JsonProperty("url")
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "ImageGiphy{" +
                "url='" + url + '\'' +
                '}';
    }
}
