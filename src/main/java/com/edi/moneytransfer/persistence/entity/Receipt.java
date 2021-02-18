package com.edi.moneytransfer.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Setter
public class Receipt extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(scale = 2)
    private BigDecimal amount;

    @ManyToOne
    private Account account;
    private Type type;

    public enum Type{
        CASH_IN, CASH_OUT
    }
}
