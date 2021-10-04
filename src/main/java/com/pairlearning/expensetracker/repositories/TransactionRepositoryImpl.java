package com.pairlearning.expensetracker.repositories;

import com.pairlearning.expensetracker.entities.Transaction;
import com.pairlearning.expensetracker.exceptions.ETBadRequestException;
import com.pairlearning.expensetracker.exceptions.ETResourceNotFoundException;
import com.pairlearning.expensetracker.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private static final String SQL_CREATE = "insert into transactions (id, category_id, user_id, amount, note, transaction_date) values(nextval('transactions_seq'), ?, ?, ?, ?, ?);";
    private static final String SQL_FIND_BY_ID = "select * from transactions where user_id = ? and category_id = ? and id = ?;";
    private static final String SQL_FIND_ALL = "select * from transactions where user_id = ? and category_id = ?";
    private static final String SQL_UPDATE = "update transactions set amount = ?, note = ?, transaction_date = ? where user_id = ? and category_id = ? and id = ?;";
    private static final String SQL_DELETE = "delete from transactions where user_id = ? and category_id = ? and id = ?;";

    @Autowired
    JdbcTemplate jdbcTemplate;
    private RowMapper<Transaction> transactionRowMapper = ((rs, rowNum) -> {
        return new Transaction(
                rs.getInt("id"),
                rs.getInt("category_id"),
                rs.getInt("user_id"),
                rs.getDouble("amount"),
                rs.getString("note"),
                rs.getTimestamp("transaction_date")
        );
    });

    @Override
    public List<Transaction> findAll(Integer userId, Integer categoryId) {
        try {
            return jdbcTemplate.query(SQL_FIND_ALL, new Object[]{userId, categoryId}, transactionRowMapper);
        } catch (Exception e) {
            System.out.println(e);
            throw new ETResourceNotFoundException("Transaction not found");
        }
    }

    @Override
    public Transaction findById(Integer userId, Integer categoryId, Integer transactionId) throws ETResourceNotFoundException {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[]{userId, categoryId, transactionId}, transactionRowMapper);
        } catch (Exception e) {
            System.out.println(e);
            throw new ETResourceNotFoundException("Transaction not found");
        }
    }

    @Override
    public Integer create(Integer userId, Integer categoryId, Double amount, String note, Timestamp transactionDate) throws ETBadRequestException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, categoryId);
                ps.setInt(2, userId);
                ps.setDouble(3, amount);
                ps.setString(4, note);
                ps.setTimestamp(5, transactionDate);
                return ps;
            }, keyHolder);
            return (Integer) keyHolder.getKeys().get("id");
        } catch (Exception e) {
            System.out.println(e);
            throw new ETBadRequestException("Invalid request");
        }
    }

    @Override
    public void update(Integer userId, Integer categoryId, Integer transactionId, Transaction transaction) throws ETBadRequestException {
        try {
            jdbcTemplate.update(SQL_UPDATE, new Object[]{transaction.getAmount(), transaction.getNote(), transaction.getTransactionDate(), userId, categoryId, transactionId});
        }catch (Exception e){
            System.out.println(e);
            throw new ETBadRequestException("Invalid request");
        }
    }

    @Override
    public void removeById(Integer userId, Integer categoryId, Integer transactionId) throws ETResourceNotFoundException {
        int count = jdbcTemplate.update(SQL_DELETE, new Object[]{userId, categoryId, transactionId});
        if(count == 0) throw new ETResourceNotFoundException("Transaction not found");
    }
}
