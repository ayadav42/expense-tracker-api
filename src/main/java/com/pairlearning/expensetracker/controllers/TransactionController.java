package com.pairlearning.expensetracker.controllers;

import com.pairlearning.expensetracker.Constants;
import com.pairlearning.expensetracker.entities.Transaction;
import com.pairlearning.expensetracker.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(Constants.API_BASE + Constants.API_CATEGORIES + "/{categoryId}/transactions")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @GetMapping("")
    public ResponseEntity<List<Transaction>> getAllTransactions(HttpServletRequest request, @PathVariable("categoryId") Integer categoryId) {
        int userId = (Integer) request.getAttribute("userId");
        List<Transaction> transactions = transactionService.fetchAllTransactions(userId, categoryId);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(HttpServletRequest request, @PathVariable("categoryId") Integer categoryId, @PathVariable("id") Integer transactionId) {
        int userId = (Integer) request.getAttribute("userId");
        Transaction transaction = transactionService.fetchTransactionById(userId, categoryId, transactionId);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Transaction> addTransaction(HttpServletRequest request, @PathVariable("categoryId") Integer categoryId, @RequestBody Map<String, Object> transactionMap) {
        int userId = (Integer) request.getAttribute("userId");
        Double amount = Double.valueOf(transactionMap.get("amount").toString());
        String note = (String) transactionMap.get("note");
        Timestamp transactionDate = new Timestamp((Long) transactionMap.get("transactionDate"));
        Transaction transaction = transactionService.addTransaction(userId, categoryId, amount, note, transactionDate);
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> updateTransaction(HttpServletRequest request, @PathVariable("categoryId") Integer categoryId, @PathVariable("id") Integer transactionId, @RequestBody Transaction transaction) {
        int userId = (Integer) request.getAttribute("userId");
        transactionService.updateTransaction(userId, categoryId, transactionId, transaction);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteTransaction(HttpServletRequest request, @PathVariable("categoryId") Integer categoryId, @PathVariable("id") Integer transactionId) {
        int userId = (Integer) request.getAttribute("userId");
        transactionService.removeTransaction(userId, categoryId, transactionId);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}
