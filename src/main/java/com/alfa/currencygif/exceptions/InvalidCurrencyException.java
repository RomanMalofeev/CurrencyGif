package com.alfa.currencygif.exceptions;

public class InvalidCurrencyException extends Exception {
    @Override
    public String getMessage() {
        return "Invalid currency: an incorrect currency has been sent, please check the available currencies in the documentation";
    }
}
