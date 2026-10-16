package com.ciicc.BanCU.service;

import com.ciicc.BanCU.dto.BalanceResponse;
import com.ciicc.BanCU.entity.Account;
import com.ciicc.BanCU.exception.AccountNotFoundException;
import com.ciicc.BanCU.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public BalanceResponse getBalance(String accountNumber) {

        Account account = accountRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new AccountNotFoundException(accountNumber));

        return BalanceResponse.builder()
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .build();
    }
}