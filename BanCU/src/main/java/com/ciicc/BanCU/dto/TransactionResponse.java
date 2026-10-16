package com.ciicc.BanCU.dto;

import com.ciicc.BanCU.enums.TransactionType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class TransactionResponse {
    private String referenceNumber;
    private TransactionType type;
    private BigDecimal amount;
    private BigDecimal balanceAfter;
    private String relatedAccountNumber;
    private LocalDateTime createdAt;
}
