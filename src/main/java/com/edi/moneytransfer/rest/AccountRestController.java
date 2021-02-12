package com.edi.moneytransfer.rest;

import com.edi.moneytransfer.application.dto.AccountDto;
import com.edi.moneytransfer.domain.model.MoneyAmount;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/account")
public class AccountRestController {

    @PutMapping
    public AccountDto topUpAccount(@RequestBody MoneyAmount moneyAmount){

        return null;
    }
}
