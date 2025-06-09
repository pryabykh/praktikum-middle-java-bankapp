package com.pryabykh.bankapp.front.service;

import com.pryabykh.bankapp.front.dto.CreateUserDto;
import com.pryabykh.bankapp.front.feign.accounts.AccountsFeignClient;
import com.pryabykh.bankapp.front.feign.accounts.ResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class UserService {
    private final AccountsFeignClient accountsFeignClient;

    public UserService(AccountsFeignClient accountsFeignClient) {
        this.accountsFeignClient = accountsFeignClient;
    }

    public String createUser(CreateUserDto createUserDto, Model model) {
        ResponseDto response = accountsFeignClient.createUser(createUserDto);
        if (response.isHasErrors()) {
            model.addAttribute("errors", response.getErrors());
            model.addAttribute("login", createUserDto.getLogin());
            model.addAttribute("name", createUserDto.getName());
            model.addAttribute("birthdate", createUserDto.getBirthdate());
            return "signup";
        } else {
            return "redirect:/";
        }
    }

    public boolean updatePassword(Long userId, String newPassword) {
        return false;
    }
}
