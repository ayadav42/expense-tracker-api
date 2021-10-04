package com.pairlearning.expensetracker.services;

import com.pairlearning.expensetracker.entities.Transaction;
import com.pairlearning.expensetracker.exceptions.ETBadRequestException;
import com.pairlearning.expensetracker.exceptions.ETResourceNotFoundException;
import com.pairlearning.expensetracker.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public List<Transaction> fetchAllTransactions(Integer userId, Integer categoryId) {
        return transactionRepository.findAll(userId, categoryId);
    }

    @Override
    public Transaction fetchTransactionById(Integer userId, Integer categoryId, Integer transactionId) throws ETResourceNotFoundException {
        return transactionRepository.findById(userId, categoryId, transactionId);
    }

    @Override
    public Transaction addTransaction(Integer userId, Integer categoryId, Double amount, String note, Timestamp transactionDate) throws ETBadRequestException {
        int transactionId = transactionRepository.create(userId, categoryId, amount, note, transactionDate);
        return transactionRepository.findById(userId, categoryId, transactionId);
    }

    @Override
    public void updateTransaction(Integer userId, Integer categoryId, Integer transactionId, Transaction transaction) throws ETBadRequestException {
        transactionRepository.update(userId, categoryId, transactionId, transaction);
    }

    @Override
    public void removeTransaction(Integer userId, Integer categoryId, Integer transactionId) throws ETResourceNotFoundException {
        transactionRepository.removeById(userId, categoryId, transactionId);
    }
}
