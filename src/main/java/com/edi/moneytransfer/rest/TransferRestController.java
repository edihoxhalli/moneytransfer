package com.edi.moneytransfer.rest;

import com.edi.moneytransfer.application.dto.AccountDto;
import com.edi.moneytransfer.application.dto.TransferDto;
import com.edi.moneytransfer.application.service.TransferService;
import com.edi.moneytransfer.application.service.UserService;
import com.edi.moneytransfer.persistence.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@RequestMapping(path = "/transfer")
@AllArgsConstructor
@Validated
public class TransferRestController {
    private final TransferService transferService;
    private final UserService userService;

    @PostMapping
    public AccountDto transferMoney(@Valid @RequestBody TransferDto transferDto){
        User recipientUser = userService.findByUsername(transferDto.getRecipientUsername());
        if(recipientUser == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "User with username: "+transferDto.getRecipientUsername()+ " does not exist!");

        return transferService.transferMoney(recipientUser, new BigDecimal(transferDto.getAmount()));
    }

}
