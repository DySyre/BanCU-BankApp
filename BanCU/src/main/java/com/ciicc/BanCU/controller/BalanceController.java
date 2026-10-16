package com.ciicc.BanCU.controller;

import com.ciicc.BanCU.dto.BalanceResponse;
import com.ciicc.BanCU.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService balanceService;

    @GetMapping("/{accountNumber}/balance")
    public BalanceResponse getBalance(
            @PathVariable String accountNumber) {
        return balanceService.getBalance(accountNumber);
    }
}