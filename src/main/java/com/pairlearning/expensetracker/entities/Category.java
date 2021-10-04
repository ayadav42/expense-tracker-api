package com.pairlearning.expensetracker.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //automcatically creates getters and setters
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    private Integer id;
    private Integer userId;
    private String title;
    private String description;
    private Double totalExpense;

}
