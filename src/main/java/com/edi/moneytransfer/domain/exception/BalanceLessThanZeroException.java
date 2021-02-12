package com.edi.moneytransfer.domain.exception;

public class BalanceLessThanZeroException extends RuntimeException {
    public BalanceLessThanZeroException(String message){
        super(message);
    }
}
