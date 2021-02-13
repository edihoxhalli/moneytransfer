package com.edi.moneytransfer.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDto {
    private Long id;
    private Double balance;
    private UserDto userDto;
}
