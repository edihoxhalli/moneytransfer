package com.edi.moneytransfer.domain.model;

import com.edi.moneytransfer.domain.exception.InsufficientFundsException;
import com.edi.moneytransfer.domain.exception.InvalidAggregateInitializationException;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class UserAccountAggregate extends Root<AccountHolder> {
    private String username;
    private Account account;

    public static class Builder{
        private Long userId;
        private String username;
        private Long accountId;
        private MoneyAmount balance;

        public Builder userId(Long userId){
            this.userId = userId;
            return this;
        }

        public Builder username(String username){
            this.username = username;
            return this;
        }

        public Builder accountId(Long accountId){
            this.accountId = accountId;
            return this;
        }

        public Builder balance(MoneyAmount balance){
            this.balance = balance;
            return this;
        }

        public UserAccountAggregate build(){
            if(this.userId == null || this.username == null || this.accountId == null || this.balance == null)
                throw new InvalidAggregateInitializationException("User Account Aggregate cannot be initialized! Parameters must not be null!");
            return new UserAccountAggregate(this);
        }
    }

    protected UserAccountAggregate(Builder builder){
        super(new AccountHolder(builder.userId));
        this.username = builder.username;
        this.account = new Account(builder.accountId, builder.balance);
    }

    public void increaseAccountBalance(MoneyAmount moneyAmount){
        this.account.addAmount(moneyAmount);
    }

    public void decreaseAccountBalance(MoneyAmount moneyAmount){
        this.account.subtractAmount(moneyAmount);
        if(this.account.getBalance().getAmount().compareTo(BigDecimal.ZERO) < 0)
            throw new InsufficientFundsException("Account with account holder username: "+ this.getUsername()+" cannot have negative balance!");
    }
}
