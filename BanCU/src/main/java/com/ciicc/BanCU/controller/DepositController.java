package com.ciicc.BanCU.controller;

import com.ciicc.BanCU.dto.MoneyRequest;
import com.ciicc.BanCU.service.DepositService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class DepositController {

    private final DepositService depositService;

    @PostMapping("/{accountNumber}/deposit")
    public ResponseEntity<Map<String, String>> deposit(
            @PathVariable String accountNumber,
            @Valid @RequestBody MoneyRequest request) {

        depositService.deposit(accountNumber, request.getAmount());

        return ResponseEntity.ok(
                Map.of("message", "Deposit successful."));
    }
}