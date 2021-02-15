package com.edi.moneytransfer.persistence.repository;

import com.edi.moneytransfer.persistence.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
}
