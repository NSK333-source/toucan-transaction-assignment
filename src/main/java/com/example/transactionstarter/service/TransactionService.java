package com.example.transactionstarter.service;

import com.example.transactionstarter.dto.CreateTransactionRequest;
import com.example.transactionstarter.entity.Transaction;
import com.example.transactionstarter.enums.TransactionStatus;
import com.example.transactionstarter.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction createTransaction(CreateTransactionRequest request) {

        if (transactionRepository.existsById(request.getTransactionId())) {
            throw new IllegalArgumentException("Transaction ID already exists");
        }

        Transaction transaction = new Transaction();

        transaction.setTransactionId(request.getTransactionId());
        transaction.setCustomerId(request.getCustomerId());
        transaction.setAmount(request.getAmount());
        transaction.setCurrency(request.getCurrency());
        transaction.setTransactionType(request.getTransactionType());

        transaction.setStatus(TransactionStatus.PENDING);

        return transactionRepository.save(transaction);
    }

    public Transaction getTransactionById(String transactionId) {

        return transactionRepository
                .findById(transactionId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Transaction not found"));
    }
    public List<Transaction> getTransactionsByCustomerId(String customerId) {

        return transactionRepository.findByCustomerId(customerId);
    }
}