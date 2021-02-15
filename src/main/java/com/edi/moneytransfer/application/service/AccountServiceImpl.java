package com.edi.moneytransfer.application.service;

import com.edi.moneytransfer.application.dto.AccountDto;
import com.edi.moneytransfer.application.mapper.AccountMapper;
import com.edi.moneytransfer.application.mapper.UserAccountAggregateMapper;
import com.edi.moneytransfer.domain.model.MoneyAmount;
import com.edi.moneytransfer.domain.model.UserAccountAggregate;
import com.edi.moneytransfer.persistence.entity.Account;
import com.edi.moneytransfer.persistence.entity.Receipt;
import com.edi.moneytransfer.persistence.entity.User;
import com.edi.moneytransfer.persistence.repository.AccountRepository;
import com.edi.moneytransfer.persistence.repository.ReceiptRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserService userService;
    private final UserAccountAggregateMapper userAccountAggregateMapper;
    private final AccountMapper accountMapper;
    private final ReceiptRepository receiptRepository;

    @Override
    public AccountDto topUpAccount(MoneyAmount moneyAmount) {
        User currentUser = this.userService.getCurrentlyLoggedInUser();

        UserAccountAggregate userAccountAggregate = new UserAccountAggregate.Builder()
                .userId(currentUser.getId())
                .username(currentUser.getUsername())
                .accountId(currentUser.getAccount().getId())
                .balance(
                        new MoneyAmount(currentUser.getAccount().getBalance())
                )
                .build();
        userAccountAggregate.increaseAccountBalance(moneyAmount);

        Account account = userAccountAggregateMapper.userAccountAggregateToAccountEntity(userAccountAggregate);
        account = accountRepository.save(account);

        Receipt receipt = new Receipt();
        receipt.setAccount(account);
        receipt.setAmount(moneyAmount.getAmount());
        receipt.setType(Receipt.Type.CASH_IN);
        receiptRepository.save(receipt);

        return accountMapper.toDto(account);
    }
}
