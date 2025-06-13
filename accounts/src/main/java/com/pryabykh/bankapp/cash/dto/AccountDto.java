package com.pryabykh.bankapp.cash.dto;

import com.pryabykh.bankapp.cash.feign.exchange.RateDto;

public class AccountDto {

    private boolean exists;

    private Long balance;

    private RateDto currency;

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public RateDto getCurrency() {
        return currency;
    }

    public void setCurrency(RateDto currency) {
        this.currency = currency;
    }
}
