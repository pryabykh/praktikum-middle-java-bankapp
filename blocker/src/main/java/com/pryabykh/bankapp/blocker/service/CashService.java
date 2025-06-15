package com.pryabykh.bankapp.blocker.service;

import com.pryabykh.bankapp.blocker.dto.CashDto;
import com.pryabykh.bankapp.blocker.dto.ResponseDto;
import com.pryabykh.bankapp.blocker.feign.accounts.AccountsFeignClient;
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
