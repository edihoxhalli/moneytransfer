package com.edi.moneytransfer.application.service;

import com.edi.moneytransfer.application.dto.AccountDto;
import com.edi.moneytransfer.application.dto.TransferDto;
import com.edi.moneytransfer.application.mapper.AccountMapper;
import com.edi.moneytransfer.application.mapper.UserAccountAggregateMapper;
import com.edi.moneytransfer.domain.model.MoneyAmount;
import com.edi.moneytransfer.domain.model.MoneyTransfer;
import com.edi.moneytransfer.domain.model.UserAccountAggregate;
import com.edi.moneytransfer.domain.service.UserAccountService;
import com.edi.moneytransfer.persistence.entity.Account;
import com.edi.moneytransfer.persistence.entity.Transfer;
import com.edi.moneytransfer.persistence.entity.User;
import com.edi.moneytransfer.persistence.repository.AccountRepository;
import com.edi.moneytransfer.persistence.repository.TransferRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class TransferServiceImpl implements TransferService {
    private final UserAccountService userAccountService;
    private final UserService userService;
    private final AccountRepository accountRepository;
    private final UserAccountAggregateMapper userAccountAggregateMapper;
    private final AccountMapper accountMapper;
    private final TransferRepository transferRepository;

    @Override
    public AccountDto transferMoney(User recipientUser, BigDecimal amount) {
        User currentUser = this.userService.getCurrentlyLoggedInUser();

        UserAccountAggregate sender = new UserAccountAggregate.Builder()
                .userId(currentUser.getId())
                .username(currentUser.getUsername())
                .accountId(currentUser.getAccount().getId())
                .balance(
                        new MoneyAmount(
                                new BigDecimal(currentUser.getAccount().getBalance())
                        )
                )
                .build();

        Account recipientAccount = recipientUser.getAccount();


        UserAccountAggregate recipient = new UserAccountAggregate.Builder()
                .userId(recipientUser.getId())
                .username(recipientUser.getUsername())
                .accountId(recipientAccount.getId())
                .balance(
                        new MoneyAmount(
                                new BigDecimal(recipientAccount.getBalance())
                        )
                )
                .build();

        MoneyTransfer moneyTransfer = userAccountService.transferMoney(sender, recipient, new MoneyAmount(amount));
        recipientAccount = userAccountAggregateMapper.userAccountAggregateToAccountEntity(moneyTransfer.getRecipient());
        Account senderAccount = userAccountAggregateMapper.userAccountAggregateToAccountEntity(moneyTransfer.getSender());

        senderAccount = accountRepository.save(senderAccount);
        recipientAccount = accountRepository.save(recipientAccount);

        Transfer transfer = new Transfer();
        transfer.setAmount(amount.doubleValue());
        transfer.setRecipient(recipientAccount);
        transfer.setSender(senderAccount);
        transferRepository.save(transfer);

        return accountMapper.toDto(senderAccount);
    }
}
