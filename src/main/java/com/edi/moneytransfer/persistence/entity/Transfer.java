package com.edi.moneytransfer.persistence.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Transfer extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Account sender;
    @ManyToOne
    private Account recipient;
    @Column(scale = 2)
    private BigDecimal amount;
}
