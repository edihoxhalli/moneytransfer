package com.edi.moneytransfer.domain.exception;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String message){
        super(message);
    }
}
