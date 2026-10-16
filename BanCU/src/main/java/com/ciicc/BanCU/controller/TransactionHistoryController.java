package com.ciicc.BanCU.controller;

import com.ciicc.BanCU.dto.TransactionResponse;
import com.ciicc.BanCU.service.TransactionHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class TransactionHistoryController {

    private final TransactionHistoryService historyService;

    @GetMapping("/{accountNumber}/transactions")
    public Page<TransactionResponse> getHistory(
            @PathVariable String accountNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return historyService.getHistory(accountNumber, page, size);
    }
}