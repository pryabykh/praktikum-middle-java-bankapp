package com.pryabykh.bankapp.cash.service;

import com.pryabykh.bankapp.cash.dto.CashDto;
import com.pryabykh.bankapp.cash.dto.ResponseDto;
import com.pryabykh.bankapp.cash.feign.accounts.AccountsFeignClient;
import org.springframework.stereotype.Service;

@Service
public class CashService {
    private final AccountsFeignClient accountsFeignClient;

    public CashService(AccountsFeignClient accountsFeignClient) {
        this.accountsFeignClient = accountsFeignClient;
    }

    public ResponseDto processCash(String login, CashDto cashDto) {
        return accountsFeignClient.processCash(login, cashDto);
    }
}
