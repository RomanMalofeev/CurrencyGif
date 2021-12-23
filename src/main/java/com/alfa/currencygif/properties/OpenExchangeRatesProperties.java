package com.alfa.currencygif.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Component
@ConfigurationProperties(prefix = "open-exchange-rates")
@Validated
public class OpenExchangeRatesProperties {
    @NotEmpty
    private String url;
    @NotEmpty
    private String appId;
    @NotEmpty
    private String base;
    @Min(value = 1)
    private int period;
    @NotEmpty
    private String dateFormat;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppId() {
        return appId;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getBase() {
        return base;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getPeriod() {
        return period;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    @Override
    public String toString() {
        return "OpenExchangeRatesProperties{" +
                "url='" + url + '\'' +
                ", appId='" + appId + '\'' +
                ", base='" + base + '\'' +
                ", period=" + period +
                '}';
    }
}
