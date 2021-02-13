package com.edi.moneytransfer.domain.model;

import lombok.*;
import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MoneyAmount {
    @NonNull
    private BigDecimal amount;
}
