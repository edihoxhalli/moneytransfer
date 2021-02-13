package com.edi.moneytransfer.application.mapper;

import com.edi.moneytransfer.domain.model.UserAccountAggregate;
import com.edi.moneytransfer.persistence.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class UserAccountAggregateMapper {
    public Account userAccountAggregateToAccountEntity(UserAccountAggregate aggregate){
        Account account = new Account();
        account.setId(aggregate.getAccount().getId());
        account.setBalance(aggregate.getAccount().getBalance().getAmount().doubleValue());
        return account;
    }
}
