package com.pryabykh.bankapp.front.service;

import com.pryabykh.bankapp.front.feign.accounts.AccountsFeignClient;
import com.pryabykh.bankapp.front.feign.accounts.UserDto;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class UserInfoModelHelper {

    private final AccountsFeignClient accountsFeignClient;

    public UserInfoModelHelper(AccountsFeignClient accountsFeignClient) {
        this.accountsFeignClient = accountsFeignClient;
    }


    public void addUserInfoToModel(Model model, String login) {
        model.addAttribute("login", login);
        UserDto user = accountsFeignClient.fetchUserByLogin(login);
        model.addAttribute("name", user.getName());
        model.addAttribute("birthdate", user.getBirthdate());
        model.addAttribute("accounts", user.getAccounts());
    }
}
