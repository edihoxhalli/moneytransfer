package com.edi.moneytransfer.domain.model;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MoneyAmount {
    @NotNull(message = "amount must not be empty!")
    @Positive(message = "amount must be positive!")
    private BigDecimal amount;
}
