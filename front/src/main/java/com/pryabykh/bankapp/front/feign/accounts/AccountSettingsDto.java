package com.pryabykh.bankapp.front.feign.accounts;

import java.time.LocalDate;

public class AccountSettingsDto {

    private String name;

    private LocalDate birthdate;

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
