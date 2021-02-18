package com.edi.moneytransfer.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TransferToSelfException extends RuntimeException {
    public TransferToSelfException(String message){
        super(message);
    }
}
