package com.edi.moneytransfer.domain.model;

public abstract class Root<R> {
    public Root(R r){
        this.root = r;
    }
    R root;
}
