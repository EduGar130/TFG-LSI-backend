package com.tfg.service;

import com.tfg.dto.TransactionDTO;
import com.tfg.entity.Transaction;
import jakarta.validation.Valid;

import java.util.List;

public interface TransactionService {
    List<Transaction> getAllTransactions();

    Transaction saveTransaction(@Valid Transaction transaction);
}
