package com.edi.moneytransfer.domain.service;

import com.edi.moneytransfer.application.dto.UserDto;
import com.edi.moneytransfer.domain.model.MoneyAmount;
import com.edi.moneytransfer.domain.model.MoneyTransfer;
import com.edi.moneytransfer.domain.model.UserAccountAggregate;
import com.edi.moneytransfer.persistence.repository.AccountRepository;
import com.edi.moneytransfer.persistence.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class UserAccountServiceImpl implements UserAccountService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;


    @Override
    public MoneyTransfer transferMoney(UserAccountAggregate sender, UserAccountAggregate recipient, MoneyAmount amount) {
        sender.decreaseAccountBalance(amount);
        recipient.increaseAccountBalance(amount);
        return new MoneyTransfer(sender, recipient, amount);
    }
}
