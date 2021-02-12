package com.edi.moneytransfer.persistence.repository;

import com.edi.moneytransfer.persistence.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
