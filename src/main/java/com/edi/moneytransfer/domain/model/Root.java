package com.edi.moneytransfer.domain.model;

import lombok.Getter;

@Getter
public abstract class Root<R> {
    public Root(R r){
        this.root = r;
    }
    R root;
}
