package com.edi.moneytransfer.application.service;

import com.edi.moneytransfer.application.dto.AccountDto;
import com.edi.moneytransfer.persistence.entity.User;

import java.math.BigDecimal;

public interface TransferService {
    AccountDto transferMoney(User recipientUser, BigDecimal amount);
}
