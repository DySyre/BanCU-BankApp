package com.ciicc.BanCU.repository;

import com.ciicc.BanCU.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

    Page<Transaction> findByAccount_AccountNumberOrderByCreatedAtDesc(
            String accountNumber,
            Pageable pageable
    );
}