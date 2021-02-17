package com.edi.moneytransfer.application.dto;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class UserDto {
    private Long id;
    @NotNull(message = "username must not be empty!")
    @NotEmpty(message = "username must not be empty!")
    private String username;

    @NotNull(message = "password must not be empty!")
    @NotEmpty(message = "password must not be empty!")
    private String password;
    private AccountDto account;


}
