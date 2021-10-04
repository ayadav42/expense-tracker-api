package com.pairlearning.expensetracker.services;

import com.pairlearning.expensetracker.entities.User;
import com.pairlearning.expensetracker.exceptions.ETAuthException;

public interface UserService {

    User validateUser(String email, String password) throws ETAuthException;

    User registerUser(String firstname, String lastname, String email, String password) throws ETAuthException;

}
