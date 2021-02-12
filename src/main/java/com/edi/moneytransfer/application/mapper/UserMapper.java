package com.edi.moneytransfer.application.mapper;

import com.edi.moneytransfer.persistence.entity.User;
import com.edi.moneytransfer.application.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = AccountMapper.class)
public abstract class UserMapper {
    public abstract User toEntity(UserDto userDto);
    public abstract UserDto toDto(User user);
}
