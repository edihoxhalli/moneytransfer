package com.edi.moneytransfer.application.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    @NonNull
    private String username;
    @NonNull
    private String password;
    private AccountDto account;
}
