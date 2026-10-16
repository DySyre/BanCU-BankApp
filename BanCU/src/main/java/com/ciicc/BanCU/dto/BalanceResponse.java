package com.ciicc.BanCU.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class BalanceResponse {
    private String accountNumber;
    private BigDecimal balance;
}