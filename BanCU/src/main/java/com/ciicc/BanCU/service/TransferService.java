package com.ciicc.BanCU.service;

import com.ciicc.BanCU.entity.Account;
import com.ciicc.BanCU.enums.AccountStatus;
import com.ciicc.BanCU.enums.TransactionType;
import com.ciicc.BanCU.exception.AccountNotFoundException;
import com.ciicc.BanCU.exception.InsufficientFundsException;
import com.ciicc.BanCU.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransactionRecorder transactionRecorder;

    @Transactional
    public void transfer(
            String sourceNumber,
            String destinationNumber,
            BigDecimal amount) {

        if (amount == null || amount.signum() <= 0
                || amount.scale() > 2) {
            throw new IllegalArgumentException(
                    "Amount must be positive and have at most 2 decimal places.");
        }

        if (sourceNumber.equals(destinationNumber)) {
            throw new IllegalArgumentException(
                    "Source and destination must be different.");
        }

        // Lock accounts in a consistent order to reduce deadlocks.
        String first = sourceNumber.compareTo(destinationNumber) < 0
                ? sourceNumber : destinationNumber;
        String second = sourceNumber.compareTo(destinationNumber) < 0
                ? destinationNumber : sourceNumber;

        Account firstAccount = accountRepository
                .findByAccountNumberForUpdate(first)
                .orElseThrow(() -> new AccountNotFoundException(first));

        Account secondAccount = accountRepository
                .findByAccountNumberForUpdate(second)
                .orElseThrow(() -> new AccountNotFoundException(second));

        Account source = sourceNumber.equals(first)
                ? firstAccount : secondAccount;
        Account destination = destinationNumber.equals(first)
                ? firstAccount : secondAccount;

        if (source.getStatus() != AccountStatus.ACTIVE
                || destination.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalArgumentException(
                    "Both accounts must be active.");
        }

        if (source.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        }

        source.setBalance(source.getBalance().subtract(amount));
        destination.setBalance(destination.getBalance().add(amount));

        transactionRecorder.record(
                source, destination,
                TransactionType.TRANSFER_OUT, amount);

        transactionRecorder.record(
                destination, source,
                TransactionType.TRANSFER_IN, amount);
    }
}