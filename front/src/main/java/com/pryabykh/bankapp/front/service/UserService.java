package com.pryabykh.bankapp.front.service;

import com.pryabykh.bankapp.front.dto.CreateUserDto;
import com.pryabykh.bankapp.front.feign.accounts.AccountSettingsDto;
import com.pryabykh.bankapp.front.feign.accounts.AccountsFeignClient;
import com.pryabykh.bankapp.front.feign.accounts.ResponseDto;
import com.pryabykh.bankapp.front.feign.accounts.UpdatePasswordDto;
import com.pryabykh.bankapp.front.feign.accounts.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Service
public class UserService {
    private final AccountsFeignClient accountsFeignClient;
    private final UserDetailsServiceImpl userDetailsService;

    public UserService(AccountsFeignClient accountsFeignClient,
                       UserDetailsServiceImpl userDetailsService) {
        this.accountsFeignClient = accountsFeignClient;
        this.userDetailsService = userDetailsService;
    }

    public String createUser(CreateUserDto createUserDto, Model model, HttpServletRequest request) {
        ResponseDto response = accountsFeignClient.createUser(createUserDto);
        if (response.isHasErrors()) {
            model.addAttribute("errors", response.getErrors());
            model.addAttribute("login", createUserDto.getLogin());
            model.addAttribute("name", createUserDto.getName());
            model.addAttribute("birthdate", createUserDto.getBirthdate());
            return "signup";
        } else {
            model.addAttribute("login", createUserDto.getLogin());
            model.addAttribute("name", createUserDto.getName());
            model.addAttribute("birthdate", createUserDto.getBirthdate());
            authenticateUser(createUserDto, request);
            return "redirect:/";
        }
    }

    public String updatePassword(String login,
                                 UpdatePasswordDto updatePasswordDto,
                                 Model model) {
        ResponseDto response = accountsFeignClient.updatePassword(login, updatePasswordDto);
        if (response.isHasErrors()) {
            model.addAttribute("passwordErrors", response.getErrors());
        }
        UserDto user = accountsFeignClient.fetchUserByLogin(login);
        model.addAttribute("login", login);
        model.addAttribute("name", user.getName());
        model.addAttribute("birthdate", user.getBirthdate());
        return "main";
    }

    public String editUserAccounts(String login,
                                   AccountSettingsDto accountSettingsDto,
                                 Model model) {
        ResponseDto response = accountsFeignClient.editUserAccounts(login, accountSettingsDto);
        if (response.isHasErrors()) {
            model.addAttribute("userAccountsErrors", response.getErrors());
        }
        UserDto user = accountsFeignClient.fetchUserByLogin(login);
        model.addAttribute("login", login);
        model.addAttribute("name", user.getName());
        model.addAttribute("birthdate", user.getBirthdate());
        return "main";
    }

    private void authenticateUser(CreateUserDto createUserDto, HttpServletRequest request) {
        UserDetails principal = userDetailsService.loadUserByUsername(createUserDto.getLogin());
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        HttpSession session = request.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, context);
    }
}
