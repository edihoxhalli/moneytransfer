package com.edi.moneytransfer.application.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransferDto {
    private Long id;
    @NonNull
    private String recipientUsername;
    @NonNull
    private Double amount;
}
