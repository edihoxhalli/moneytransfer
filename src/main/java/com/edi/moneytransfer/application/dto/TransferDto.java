package com.edi.moneytransfer.application.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransferDto {
    private Long id;
    @NotNull(message = "recipientUsername must not be empty!")
    private String recipientUsername;
    @NotNull(message = "amount must not be empty!")
    @Positive(message = "amount must be positive!")
    private BigDecimal amount;
}
