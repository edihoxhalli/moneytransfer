package com.edi.moneytransfer.domain.exception;

public class InvalidAggregateInitializationException extends RuntimeException{
    public InvalidAggregateInitializationException(String message){
        super(message);
    }
}
