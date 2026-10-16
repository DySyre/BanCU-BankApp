package com.ciicc.BanCU.dto;

import com.ciicc.BanCU.enums.AccountStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class AccountResponse {
    private String accountNumber;
    private String accountHolder;
    private BigDecimal balance;
    private AccountStatus status;
}