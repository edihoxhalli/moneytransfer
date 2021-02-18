package com.edi.moneytransfer.persistence.repository;

import com.edi.moneytransfer.persistence.entity.Account;
import com.edi.moneytransfer.persistence.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    List<Receipt> findByAccount(Account account);
}
