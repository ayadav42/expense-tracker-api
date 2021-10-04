package com.pairlearning.expensetracker.services;

import com.pairlearning.expensetracker.entities.Transaction;
import com.pairlearning.expensetracker.exceptions.ETBadRequestException;
import com.pairlearning.expensetracker.exceptions.ETResourceNotFoundException;

import java.sql.Timestamp;
import java.util.List;

public interface TransactionService {

    List<Transaction> fetchAllTransactions(Integer userId, Integer categoryId);

    Transaction fetchTransactionById(Integer userId, Integer categoryId, Integer transactionId) throws ETResourceNotFoundException;

    Transaction addTransaction(Integer userId, Integer categoryId, Double amount, String note, Timestamp transactionDate) throws ETBadRequestException;

    void updateTransaction(Integer userId, Integer categoryId, Integer transactionId, Transaction transaction) throws ETBadRequestException;

    void removeTransaction(Integer userId, Integer categoryId, Integer transactionId) throws ETResourceNotFoundException;

}
