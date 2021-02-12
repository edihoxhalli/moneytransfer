package com.edi.moneytransfer.domain.service;

import com.edi.moneytransfer.application.dto.UserDto;
import com.edi.moneytransfer.domain.model.UserAccountAggregate;
import com.edi.moneytransfer.persistence.repository.AccountRepository;
import com.edi.moneytransfer.persistence.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @Override
    public UserDto createUserAccountAggregate(UserDto userDto) {

//        new UserAccountAggregate(userDto.getId(), userDto.getFirstName(), userDto.getLastName(), );
        return null;
    }

    @Override
    public UserAccountAggregate getUserAccountAggregate() {
        return null;
    }
}
