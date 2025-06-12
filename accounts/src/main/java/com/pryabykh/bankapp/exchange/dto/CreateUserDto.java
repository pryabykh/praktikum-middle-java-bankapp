package com.pryabykh.bankapp.exchange.dto;

import java.time.LocalDate;

public class CreateUserDto {

    private String login;

    private String password;

    private String confirmPassword;

    private String name;

    private LocalDate birthdate;

    public CreateUserDto(String login, String password, String confirmPassword, String name, LocalDate birthdate) {
        this.login = login;
        this.password = password;
        this.confirmPassword = confirmPassword;
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
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
}
