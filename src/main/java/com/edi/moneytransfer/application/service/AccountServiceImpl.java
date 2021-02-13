package com.edi.moneytransfer.application.service;

import com.edi.moneytransfer.application.dto.AccountDto;
import com.edi.moneytransfer.application.mapper.AccountMapper;
import com.edi.moneytransfer.application.mapper.UserAccountAggregateMapper;
import com.edi.moneytransfer.domain.model.MoneyAmount;
import com.edi.moneytransfer.domain.model.UserAccountAggregate;
import com.edi.moneytransfer.persistence.entity.Account;
import com.edi.moneytransfer.persistence.entity.User;
import com.edi.moneytransfer.persistence.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserService userService;
    private final UserAccountAggregateMapper userAccountAggregateMapper;
    private final AccountMapper accountMapper;

    @Override
    public AccountDto topUpAccount(MoneyAmount moneyAmount) {
        User currentUser = this.userService.getCurrentlyLoggedInUser();

        UserAccountAggregate userAccountAggregate = new UserAccountAggregate.Builder()
                .userId(currentUser.getId())
                .username(currentUser.getUsername())
                .accountId(currentUser.getAccount().getId())
                .balance(
                        new MoneyAmount(
                                new BigDecimal(currentUser.getAccount().getBalance())
                        )
                )
                .build();
        userAccountAggregate.increaseAccountBalance(moneyAmount);

        Account account = userAccountAggregateMapper.userAccountAggregateToAccountEntity(userAccountAggregate);
        account = accountRepository.save(account);

        return accountMapper.toDto(account);
    }
}
