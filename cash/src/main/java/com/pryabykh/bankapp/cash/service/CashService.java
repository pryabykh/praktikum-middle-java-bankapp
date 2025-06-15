package com.pryabykh.bankapp.cash.service;

import com.pryabykh.bankapp.cash.dto.CashDto;
import com.pryabykh.bankapp.cash.dto.ResponseDto;
import com.pryabykh.bankapp.cash.feign.accounts.AccountsFeignClient;
import com.pryabykh.bankapp.cash.feign.blocker.BlockerFeignClient;
import org.springframework.stereotype.Service;

@Service
public class CashService {
    private final AccountsFeignClient accountsFeignClient;
    private final BlockerFeignClient blockerFeignClient;

    public CashService(AccountsFeignClient accountsFeignClient, BlockerFeignClient blockerFeignClient) {
        this.accountsFeignClient = accountsFeignClient;
        this.blockerFeignClient = blockerFeignClient;
    }

    public ResponseDto processCash(String login, CashDto cashDto) {
        boolean suspicious = blockerFeignClient.isSuspicious();
        if (!suspicious) {
            return accountsFeignClient.processCash(login, cashDto);
        } else {
            ResponseDto response = new ResponseDto();
            response.setHasErrors(true);
            response.getErrors().add("⛔⛔⛔ Подозрительная операция заблокирована ⛔⛔⛔");
            return response;
        }
    }
}
