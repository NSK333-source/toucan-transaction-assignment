package com.example.transactionstarter.controller;

import com.example.transactionstarter.dto.CreateTransactionRequest;
import com.example.transactionstarter.entity.Transaction;
import com.example.transactionstarter.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.transactionstarter.dto.UpdateTransactionStatusRequest;
import org.springframework.web.bind.annotation.PatchMapping;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(
            @Valid @RequestBody CreateTransactionRequest request) {

        Transaction transaction = transactionService.createTransaction(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transaction);
    }
    @GetMapping("/{transactionId}")
    public ResponseEntity<Transaction> getTransactionById(
            @PathVariable String transactionId) {

        Transaction transaction =
                transactionService.getTransactionById(transactionId);

        return ResponseEntity.ok(transaction);
    }
    @GetMapping("/customer/{customerId}")
    public List<Transaction> getTransactionsByCustomerId(
            @PathVariable String customerId) {

        return transactionService.getTransactionsByCustomerId(customerId);
    }
    @PatchMapping("/{transactionId}/status")
    public Transaction updateTransactionStatus(
            @PathVariable String transactionId,
            @RequestBody UpdateTransactionStatusRequest request) {

        return transactionService.updateTransactionStatus(
                transactionId,
                request
        );
    }
}