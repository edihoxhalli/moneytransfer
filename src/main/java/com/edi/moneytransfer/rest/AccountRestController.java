package com.edi.moneytransfer.rest;

import com.edi.moneytransfer.application.dto.AccountDto;
import com.edi.moneytransfer.application.service.AccountService;
import com.edi.moneytransfer.domain.model.MoneyAmount;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/account")
@AllArgsConstructor
@Validated
public class AccountRestController {

    private final AccountService accountService;

    @PutMapping
    public AccountDto topUpAccount(@Valid @RequestBody MoneyAmount moneyAmount){
        return accountService.topUpAccount(moneyAmount);
    }
}
