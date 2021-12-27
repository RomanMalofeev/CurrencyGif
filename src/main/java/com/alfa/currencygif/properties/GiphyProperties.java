/*
 * Copyright (c) 2021.  Roman Malofeev
 * Junior java developer task for alfa-bank.
 */

package com.alfa.currencygif.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotEmpty;

@Component
@ConfigurationProperties(prefix = "giphy")
@Validated
public class GiphyProperties {
    @NotEmpty
    private String url;
    @NotEmpty
    private String apiKey;
    private String positiveTag;
    private String negativeTag;
    private String rating;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setPositiveTag(String positiveTag) {
        this.positiveTag = positiveTag;
    }

    public String getPositiveTag() {
        return positiveTag;
    }

    public void setNegativeTag(String negativeTag) {
        this.negativeTag = negativeTag;
    }

    public String getNegativeTag() {
        return negativeTag;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRating() {
        return rating;
    }

    @Override
    public String toString() {
        return "GiphyProperties{" +
                "url='" + url + '\'' +
                ", apiKey='" + apiKey + '\'' +
                ", positiveTag='" + positiveTag + '\'' +
                ", negativeTag='" + negativeTag + '\'' +
                ", rating='" + rating + '\'' +
                '}';
    }
}
