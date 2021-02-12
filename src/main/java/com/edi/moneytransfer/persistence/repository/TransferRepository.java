package com.edi.moneytransfer.persistence.repository;

import com.edi.moneytransfer.persistence.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
}
