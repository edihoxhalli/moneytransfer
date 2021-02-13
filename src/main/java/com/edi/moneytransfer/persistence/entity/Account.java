package com.edi.moneytransfer.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Double balance;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "account")
    private User user;
}
