package com.pairlearning.expensetracker.repositories;

import com.pairlearning.expensetracker.entities.Transaction;
import com.pairlearning.expensetracker.exceptions.ETBadRequestException;
import com.pairlearning.expensetracker.exceptions.ETResourceNotFoundException;

import java.sql.Timestamp;
import java.util.List;

public interface TransactionRepository {

    List<Transaction> findAll(Integer userId, Integer categoryId);

    Transaction findById(Integer userId, Integer categoryId, Integer transactionId) throws ETResourceNotFoundException;

    Integer create(Integer userId, Integer categoryId, Double amount, String note, Timestamp transactionDate) throws ETBadRequestException;

    void update(Integer userId, Integer categoryId, Integer transactionId, Transaction transaction) throws ETBadRequestException;

    void removeById(Integer userId, Integer categoryId, Integer transactionId) throws ETResourceNotFoundException;

}
