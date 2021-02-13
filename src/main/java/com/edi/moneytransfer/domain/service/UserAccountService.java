package com.edi.moneytransfer.domain.service;

import com.edi.moneytransfer.domain.model.MoneyAmount;
import com.edi.moneytransfer.domain.model.MoneyTransfer;
import com.edi.moneytransfer.domain.model.UserAccountAggregate;

public interface UserAccountService {
    MoneyTransfer transferMoney(UserAccountAggregate sender, UserAccountAggregate recipient, MoneyAmount amount);
}
