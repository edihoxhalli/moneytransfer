package com.edi.moneytransfer.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MoneyTransfer {
    UserAccountAggregate sender;
    UserAccountAggregate recipient;
    MoneyAmount amount;
}
