package com.edi.moneytransfer.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
public class MoneyAmount {
    private BigDecimal amount;
}
