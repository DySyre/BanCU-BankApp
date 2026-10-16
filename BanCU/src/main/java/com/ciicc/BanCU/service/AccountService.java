
package com.ciicc.BanCU.service;

import com.ciicc.BanCU.dto.AccountResponse;
import com.ciicc.BanCU.dto.CreateAccountRequest;
import com.ciicc.BanCU.entity.Account;
import com.ciicc.BanCU.enums.AccountStatus;
import com.ciicc.BanCU.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    // Create a new bank account
    @Transactional
    public AccountResponse createAccount(
            CreateAccountRequest request) {

        String accountNumber;

        do {
            accountNumber = UUID.randomUUID()
                    .toString()
                    .replace("-", "")
                    .substring(0, 12)
                    .toUpperCase();
        } while (accountRepository
                .existsByAccountNumber(accountNumber));

        BigDecimal initialDeposit =
                request.getInitialDeposit() == null
                        ? BigDecimal.ZERO
                        : request.getInitialDeposit();

        if (initialDeposit.signum() < 0) {
            throw new IllegalArgumentException(
                    "Initial deposit cannot be negative."
            );
        }

        Account account = Account.builder()
                .accountNumber(accountNumber)
                .accountHolder(request.getAccountHolder().trim())
                .balance(initialDeposit)
                .status(AccountStatus.ACTIVE)
                .build();

        Account saved = accountRepository.save(account);

        return AccountResponse.builder()
                .accountNumber(saved.getAccountNumber())
                .accountHolder(saved.getAccountHolder())
                .balance(saved.getBalance())
                .status(saved.getStatus())
                .build();
    }

    // Retrieve all bank accounts
    @Transactional(readOnly = true)
    public List<AccountResponse> getAllAccounts() {

        return accountRepository.findAll()
                .stream()
                .map(account -> AccountResponse.builder()
                        .accountNumber(account.getAccountNumber())
                        .accountHolder(account.getAccountHolder())
                        .balance(account.getBalance())
                        .status(account.getStatus())
                        .build())
                .toList();
    }
}
