package com.pryabykh.bankapp.accounts.dto;

public class AllUsersDto {

    private String login;

    private String name;

    public AllUsersDto() {
    }

    public AllUsersDto(String login, String name) {
        this.login = login;
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

}
