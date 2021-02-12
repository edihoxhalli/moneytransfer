package com.edi.moneytransfer.domain.service;

import com.edi.moneytransfer.application.dto.UserDto;
import com.edi.moneytransfer.domain.model.UserAccountAggregate;

public interface UserAccountService {
    UserDto createUserAccountAggregate(UserDto userDto);
    UserAccountAggregate getUserAccountAggregate();
}
