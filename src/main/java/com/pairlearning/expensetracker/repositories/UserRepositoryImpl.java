package com.pairlearning.expensetracker.repositories;

import com.pairlearning.expensetracker.entities.User;
import com.pairlearning.expensetracker.exceptions.ETAuthException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private static final String SQL_CREATE = "insert into users(id, firstname, lastname, email, password) values(nextval('users_seq'), ?, ?, ?, ?);";
    private static final String SQL_COUNT_BY_EMAIL = "select count(*) from users where email = ?";
    private static final String SQL_FIND_BY_ID = "select * from users where id = ?";
    private static final String SQL_FIND_BY_EMAIL = "select * from users where email = ?";

    @Autowired
    JdbcTemplate jdbcTemplate; //for DMLs
    private RowMapper<User> userRowMapper = ((rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setFirstname(rs.getString("firstname"));
        user.setLastname(rs.getString("lastname"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));

        return user;
    });

    @Override
    public Integer create(String firstname, String lastname, String email, String password) throws ETAuthException {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10)); //even if two users have same password, their hashes will be different because a new random salt is generated each time
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS); //the second argument is a flag we pass to get generated keys in return
                //the keys generated are then stored in keyHolder
                ps.setString(1, firstname);
                ps.setString(2, lastname);
                ps.setString(3, email);
                ps.setString(4, hashedPassword);
                return ps;
            }, keyHolder);

            return (Integer) keyHolder.getKeys().get("id"); //since key can be composite, we specify

        } catch (Exception e) {
            System.out.println("Exception e: " + e);
            throw new ETAuthException("Invalid details, failed to create account");
        }
    }

    @Override
    public User findByEmailAndPassword(String email, String password) throws ETAuthException {
        try {
            User user = jdbcTemplate.queryForObject(SQL_FIND_BY_EMAIL, new Object[]{email}, userRowMapper); //throws EmptyResultDataAccess Exception if the user is not found
            if(!BCrypt.checkpw(password, user.getPassword())) throw new ETAuthException("Invalid email/password");

            return user;
        }catch (Exception e){
            System.out.println(e);
            throw new ETAuthException("Invalid email/password");
        }
    }

    @Override
    public Integer getCountByEmail(String email) {
        return jdbcTemplate.queryForObject(SQL_COUNT_BY_EMAIL, new Object[]{email}, Integer.class);
        //second argument - array of object containing all params for the query
        //third argument - cast for output
    }

    @Override
    public User findById(Integer id) {
        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[]{id}, userRowMapper);
    }
}
