package com.edi.moneytransfer.application.mapper;

import com.edi.moneytransfer.persistence.entity.Account;
import com.edi.moneytransfer.application.dto.AccountDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public abstract class AccountMapper {
    public abstract Account toEntity(AccountDto accountDto);
    public abstract AccountDto toDto(Account account);
}
