package com.ciicc.BanCU.service;

import com.ciicc.BanCU.dto.TransactionResponse;
import com.ciicc.BanCU.entity.Transaction;
import com.ciicc.BanCU.repository.AccountRepository;
import com.ciicc.BanCU.repository.TransactionRepository;
import com.ciicc.BanCU.exception.AccountNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionHistoryService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional(readOnly = true)
    public Page<TransactionResponse> getHistory(
            String accountNumber, int page, int size) {

        if (page < 0 || size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Page must be non-negative and size must be between 1 and 100.");
        }

        accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new AccountNotFoundException(accountNumber));

        return transactionRepository
                .findByAccount_AccountNumberOrderByCreatedAtDesc(
                        accountNumber,
                        PageRequest.of(page, size,
                                Sort.by(Sort.Direction.DESC, "createdAt")))
                .map(this::toResponse);
    }

    private TransactionResponse toResponse(Transaction tx) {
        return TransactionResponse.builder()
                .referenceNumber(tx.getReferenceNumber())
                .type(tx.getType())
                .amount(tx.getAmount())
                .balanceAfter(tx.getBalanceAfter())
                .relatedAccountNumber(
                        tx.getRelatedAccount() == null
                                ? null
                                : tx.getRelatedAccount().getAccountNumber())
                .createdAt(tx.getCreatedAt())
                .build();
    }
}