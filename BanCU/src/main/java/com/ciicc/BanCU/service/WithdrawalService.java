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
public class WithdrawalService {

    private final AccountRepository accountRepository;
    private final TransactionRecorder transactionRecorder;

    @Transactional
    public void withdraw(String accountNumber, BigDecimal amount) {
        if (amount == null || amount.signum() <= 0
                || amount.scale() > 2) {
            throw new IllegalArgumentException(
                    "Amount must be positive and have at most 2 decimal places.");
        }

        Account account = accountRepository
                .findByAccountNumberForUpdate(accountNumber)
                .orElseThrow(() ->
                        new AccountNotFoundException(accountNumber));

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalArgumentException(
                    "Account is not active.");
        }

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        }

        account.setBalance(account.getBalance().subtract(amount));

        transactionRecorder.record(
                account, null, TransactionType.WITHDRAWAL, amount);
    }
}