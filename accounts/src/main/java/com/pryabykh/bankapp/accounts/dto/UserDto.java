package com.pryabykh.bankapp.accounts.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserDto {

    private String login;

    private String password;

    private String name;

    private LocalDate birthdate;

    private List<AccountDto> accounts = new ArrayList<>();

    public UserDto(String login, String password, String name, LocalDate birthdate) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.birthdate = birthdate;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public List<AccountDto> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountDto> accounts) {
        this.accounts = accounts;
    }
}
