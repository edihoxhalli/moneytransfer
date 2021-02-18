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

    void addAmount(MoneyAmount amount){
        BigDecimal newAmount = this.balance.getAmount().add(amount.getAmount());
        this.balance.setAmount(newAmount);
    }

    void subtractAmount(MoneyAmount amount){
        BigDecimal newAmount = this.balance.getAmount().subtract(amount.getAmount());
        this.balance.setAmount(newAmount);
    }
}
