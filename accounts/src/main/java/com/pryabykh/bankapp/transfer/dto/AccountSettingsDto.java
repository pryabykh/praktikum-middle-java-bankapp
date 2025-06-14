package com.pryabykh.bankapp.transfer.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AccountSettingsDto {

    private String name;

    private LocalDate birthdate;

    private List<String> accounts = new ArrayList<>();

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

    public List<String> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<String> accounts) {
        this.accounts = accounts;
    }
}
