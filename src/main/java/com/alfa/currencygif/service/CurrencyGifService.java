/*
 * Copyright (c) 2021.  Roman Malofeev
 * Junior java developer task for alfa-bank.
 */

package com.alfa.currencygif.service;

import com.alfa.currencygif.exceptions.InvalidCurrencyException;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface CurrencyGifService {
    String getGifPage(String currency) throws InvalidCurrencyException, JsonProcessingException;
}
