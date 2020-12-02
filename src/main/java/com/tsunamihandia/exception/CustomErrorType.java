package com.tsunamihandia.exception;

import com.tsunamihandia.dto.UsersDTO;

public class CustomErrorType extends UsersDTO {

    private final String errorMessage;

    public CustomErrorType(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
