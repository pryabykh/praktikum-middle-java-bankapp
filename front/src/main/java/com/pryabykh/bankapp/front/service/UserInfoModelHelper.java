package com.pryabykh.bankapp.front.service;

import com.pryabykh.bankapp.front.feign.accounts.AccountsFeignClient;
import com.pryabykh.bankapp.front.feign.accounts.UserDto;
import com.pryabykh.bankapp.front.feign.exchange.ExchangeFeignClient;
import com.pryabykh.bankapp.front.feign.exchange.RateDto;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class UserInfoModelHelper {

    private final AccountsFeignClient accountsFeignClient;
    private final ExchangeFeignClient exchangeFeignClient;

    public UserInfoModelHelper(AccountsFeignClient accountsFeignClient, ExchangeFeignClient exchangeFeignClient) {
        this.accountsFeignClient = accountsFeignClient;
        this.exchangeFeignClient = exchangeFeignClient;
    }


    public void addUserInfoToModel(Model model, String login) {
        model.addAttribute("login", login);
        UserDto user = accountsFeignClient.fetchUserByLogin(login);
        List<RateDto> rates = exchangeFeignClient.fetchAll();
        model.addAttribute("name", user.getName());
        model.addAttribute("birthdate", user.getBirthdate());
        model.addAttribute("accounts", user.getAccounts());
        model.addAttribute("currency", rates);
        model.addAttribute("users", accountsFeignClient.fetchAllUsers(login));
    }
}
