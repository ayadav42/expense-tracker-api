package com.pairlearning.expensetracker.repositories;

import com.pairlearning.expensetracker.entities.User;
import com.pairlearning.expensetracker.exceptions.ETAuthException;

public interface UserRepository {

    Integer create(String firstname, String lastname, String email, String password) throws ETAuthException;

    User findByEmailAndPassword(String email, String password) throws ETAuthException;

    Integer getCountByEmail(String email);

    User findById(Integer id);
}
