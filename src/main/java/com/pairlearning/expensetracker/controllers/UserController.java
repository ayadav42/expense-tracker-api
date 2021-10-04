package com.pairlearning.expensetracker.controllers;

import com.pairlearning.expensetracker.Constants;
import com.pairlearning.expensetracker.entities.User;
import com.pairlearning.expensetracker.services.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//@RestController adds both @Controller and @ResponseBody annotation to this class
@RestController
@RequestMapping(Constants.API_BASE + Constants.API_USERS)
public class UserController {

    @Autowired //just dependency injection
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody Map<String, Object> userMap){
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");
        User user = userService.validateUser(email, password);
        Map<String, String> map = new HashMap<>();
//        map.put("message", "logged in successfully");

        return new ResponseEntity<>(generateJWTToken(user), HttpStatus.OK);
    }

    @PostMapping("/register")
    //for all resource endpoints, instead of returning raw objects
    //we'll wrap them in ResponseEntity(extension of HttpEntity) to help set status code, headers etc
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody Map<String, Object> userMap) {
        String firstname = (String) userMap.get("firstname");
        String lastname = (String) userMap.get("lastname");
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");

//        return firstname + ", " + lastname + ", " + email + ", " + password;
        User user = userService.registerUser(firstname, lastname, email, password);
        Map<String, String> map = new HashMap<>();
//        map.put("message", "registered successfully");

        return new ResponseEntity<>(generateJWTToken(user), HttpStatus.OK);
    }

    private Map<String, String> generateJWTToken(User user){
        long timestamp = System.currentTimeMillis();
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, Constants.API_SECRET_KEY)
                .setIssuedAt(new Date(timestamp))
                .setExpiration(new Date(timestamp + Constants.TOKEN_VALIDITY))
                .claim("userId", user.getId())
                .claim("email", user.getEmail())
                .claim("firstname", user.getFirstname())
                .claim("lastname", user.getLastname())
                .compact(); //this builds the token
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        return map;
    }

}
