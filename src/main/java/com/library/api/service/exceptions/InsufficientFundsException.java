package com.library.api.service.exceptions;

public class InsufficientFundsException extends RuntimeException{

    public InsufficientFundsException(String msg) {
        super(msg);
    }
}
