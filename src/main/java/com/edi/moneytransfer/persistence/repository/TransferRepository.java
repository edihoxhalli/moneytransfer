package com.edi.moneytransfer.persistence.repository;

import com.edi.moneytransfer.persistence.entity.Account;
import com.edi.moneytransfer.persistence.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
    List<Transfer> findBySenderAndRecipient(Account sender, Account recipient);
}
