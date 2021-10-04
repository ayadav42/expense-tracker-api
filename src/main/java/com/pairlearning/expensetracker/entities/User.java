package com.pairlearning.expensetracker.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //automcatically creates getters and setters
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;

}
