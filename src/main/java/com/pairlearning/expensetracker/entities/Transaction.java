package com.pairlearning.expensetracker.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data //automcatically creates getters and setters
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private Integer id;
    private Integer categoryId;
    private Integer userId;
    private Double amount;
    private String note;
    private Timestamp transactionDate;
}
