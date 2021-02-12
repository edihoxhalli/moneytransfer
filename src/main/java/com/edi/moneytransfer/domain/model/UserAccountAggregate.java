package com.edi.moneytransfer.domain.model;

import lombok.Getter;

@Getter
public class UserAccountAggregate {
    private Long userId;
    private String firstName;
    private String lastName;
    private Account account;

    public static class Builder{
        private Long userId;
        private String firstName;
        private String lastName;
        private Long accountId;
        private MoneyAmount balance;

        public Builder userId(Long userId){
            this.userId = userId;
            return this;
        }

        public Builder firstName(String firstName){
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName){
            this.lastName = lastName;
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
    }

    protected UserAccountAggregate(Long userId, String firstName, String lastName, Long accountId, MoneyAmount balance){
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.account = new Account(accountId, balance);
    }

}
