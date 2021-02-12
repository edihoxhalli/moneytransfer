package com.edi.moneytransfer.domain.model;

import com.edi.moneytransfer.domain.exception.BalanceLessThanZeroException;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Account {
    private Long id;
    private MoneyAmount balance;

    Account(Long id, MoneyAmount balance){
        this.id = id;
        if (balance.getAmount().compareTo(BigDecimal.ZERO) >= 0){
            this.balance = balance;
        } else {
            throw new BalanceLessThanZeroException("Account balance cannot be less than 0!");
        }
    }
}
