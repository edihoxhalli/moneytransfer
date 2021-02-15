package com.edi.moneytransfer.domain.exception;

public class TransferToSelfException extends RuntimeException {
    public TransferToSelfException(String message){
        super(message);
    }
}
