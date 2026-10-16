package com.ciicc.BanCU.controller;

import com.ciicc.BanCU.dto.TransferRequest;
import com.ciicc.BanCU.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<Map<String, String>> transfer(
            @Valid @RequestBody TransferRequest request) {

        transferService.transfer(
                request.getSourceAccountNumber(),
                request.getDestinationAccountNumber(),
                request.getAmount());

        return ResponseEntity.ok(
                Map.of("message", "Transfer successful."));
    }
}
