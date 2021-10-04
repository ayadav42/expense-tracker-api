package com.pairlearning.expensetracker.services;

import com.pairlearning.expensetracker.entities.User;
import com.pairlearning.expensetracker.exceptions.ETAuthException;
import com.pairlearning.expensetracker.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service //for automatic bean detection
@Transactional
//provides transactional behaviour to all methods in class, method invoked = new transaction created, method end = transaction commit
//If any 'RuntimeException' is thrown, the whole transaction will be rolled back
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User validateUser(String email, String password) throws ETAuthException {
        if(email!=null) email = email.toLowerCase();
        return userRepository.findByEmailAndPassword(email, password);
    }

    @Override
    public User registerUser(String firstname, String lastname, String email, String password) throws ETAuthException {
        if (email != null) email = email.toLowerCase();

        Pattern pattern = Pattern.compile("^(.+)@(.+)$");
        if(!pattern.matcher(email).matches()) throw new ETAuthException("Invalid email format");

        Integer count = userRepository.getCountByEmail(email);
        if(count > 0) throw new ETAuthException("Email already in use");

        Integer userId = userRepository.create(firstname, lastname, email, password);
        return userRepository.findById(userId);
    }
}
