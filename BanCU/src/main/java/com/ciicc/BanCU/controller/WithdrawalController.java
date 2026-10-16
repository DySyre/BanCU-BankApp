package com.ciicc.BanCU.controller;

import com.ciicc.BanCU.dto.MoneyRequest;
import com.ciicc.BanCU.service.WithdrawalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class WithdrawalController {

    private final WithdrawalService withdrawalService;

    @PostMapping("/{accountNumber}/withdraw")
    public ResponseEntity<Map<String, String>> withdraw(
            @PathVariable String accountNumber,
            @Valid @RequestBody MoneyRequest request) {

        withdrawalService.withdraw(accountNumber, request.getAmount());

        return ResponseEntity.ok(
                Map.of("message", "Withdrawal successful."));
    }
}