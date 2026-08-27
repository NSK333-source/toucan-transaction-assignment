package com.example.transactionstarter.dto;

import com.example.transactionstarter.enums.TransactionStatus;

public class UpdateTransactionStatusRequest {

    private TransactionStatus status;

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }
}