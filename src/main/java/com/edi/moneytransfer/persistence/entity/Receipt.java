package com.edi.moneytransfer.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Setter
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    @ManyToOne
    private Account account;
    private Type type;

    public enum Type{
        CASH_IN, CASH_OUT
    }
}
