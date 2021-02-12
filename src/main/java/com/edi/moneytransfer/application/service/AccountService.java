package com.edi.moneytransfer.application.service;

import com.edi.moneytransfer.application.dto.AccountDto;
import com.edi.moneytransfer.domain.model.MoneyAmount;

public interface AccountService {
    AccountDto topUpAccount(MoneyAmount moneyAmount);
}
