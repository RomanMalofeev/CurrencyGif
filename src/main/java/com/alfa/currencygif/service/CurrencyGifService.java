package com.alfa.currencygif.service;

import com.alfa.currencygif.exceptions.InvalidCurrencyException;

public interface CurrencyGifService {
    String getGifPage(String currency) throws InvalidCurrencyException;
}
