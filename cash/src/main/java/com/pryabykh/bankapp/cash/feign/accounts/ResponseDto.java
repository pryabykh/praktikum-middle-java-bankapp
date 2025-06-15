package com.pryabykh.bankapp.cash.feign.accounts;

import java.util.ArrayList;
import java.util.List;

public class ResponseDto {

    private boolean hasErrors;

    private final List<String> errors = new ArrayList<>();

    public boolean isHasErrors() {
        return hasErrors;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
