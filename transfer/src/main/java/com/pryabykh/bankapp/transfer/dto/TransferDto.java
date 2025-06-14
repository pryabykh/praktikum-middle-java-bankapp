package com.pryabykh.bankapp.transfer.dto;

public class TransferDto {

    private String fromCurrency;

    private String toCurrency;

    private String toLogin;

    private Long value;

    private Long convertedValue;

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public String getToLogin() {
        return toLogin;
    }

    public void setToLogin(String toLogin) {
        this.toLogin = toLogin;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public Long getConvertedValue() {
        return convertedValue;
    }

    public void setConvertedValue(Long convertedValue) {
        this.convertedValue = convertedValue;
    }
}
