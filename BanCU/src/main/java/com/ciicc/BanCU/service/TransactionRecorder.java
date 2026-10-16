package com.ciicc.BanCU.service;

import com.ciicc.BanCU.entity.Account;
import com.ciicc.BanCU.entity.Transaction;
import com.ciicc.BanCU.enums.TransactionType;
import com.ciicc.BanCU.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TransactionRecorder {

    private final TransactionRepository transactionRepository;

    public void record(
            Account account,
            Account relatedAccount,
            TransactionType type,
            BigDecimal amount) {

        Transaction transaction = Transaction.builder()
                .account(account)
                .relatedAccount(relatedAccount)
                .type(type)
                .amount(amount)
                .balanceAfter(account.getBalance())
                .referenceNumber(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);
    }
}