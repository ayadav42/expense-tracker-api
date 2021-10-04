package com.pairlearning.expensetracker.repositories;

import com.pairlearning.expensetracker.entities.Category;
import com.pairlearning.expensetracker.exceptions.ETBadRequestException;
import com.pairlearning.expensetracker.exceptions.ETResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

    private static final String SQL_CREATE = "insert into categories (id, user_id, title, description) values(nextval('categories_seq'), ? , ? , ?);";
    private static final String SQL_FIND_BY_ID = "select c.id, c.user_id, c.title, c.description, coalesce(sum(t.amount), 0) total_expense " +
            "from TRANSACTIONS t right outer join CATEGORIES c on c.id = t.category_id " +
            "where c.user_id = ? and c.id = ? group by c.id;";
    private static final String SQL_FIND_ALL = "select c.id, c.user_id, c.title, c.description, coalesce(sum(t.amount), 0) total_expense " +
            "from TRANSACTIONS t right outer join CATEGORIES c on c.id = t.category_id " +
            "where c.user_id = ? group by c.id;";
    private static final String SQL_UPDATE = "update categories set title = ?, description = ? where user_id = ? and id = ?;";
    private static final String SQL_DELETE = "delete from categories where user_id = ? and id = ?;";
    private static final String SQL_DELETE_ALL_RELATED_TRANSACTIONS = "delete from transactions where category_id = ?;";

    @Autowired
    JdbcTemplate jdbcTemplate;
    private RowMapper<Category> categoryRowMapper = ((rs, rowNum) -> {
        return new Category(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getDouble("total_expense"));
    });

    @Override
    public List<Category> findAll(Integer userId) throws ETResourceNotFoundException {
        return jdbcTemplate.query(SQL_FIND_ALL, new Object[]{userId}, categoryRowMapper);
    }

    @Override
    public Category findById(Integer userId, Integer categoryId) throws ETResourceNotFoundException {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[]{userId, categoryId}, categoryRowMapper);
        } catch (Exception e) {
            System.out.println(e);
            throw new ETResourceNotFoundException("Category not found");
        }
    }

    @Override
    public Integer create(Integer userId, String title, String description) throws ETBadRequestException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, userId);
                ps.setString(2, title);
                ps.setString(3, description);
                return ps;
            }, keyHolder);

            return (Integer) keyHolder.getKeys().get("id");
        } catch (Exception e) {
            System.out.println(e);
            throw new ETBadRequestException("Invalid request");
        }
    }

    @Override
    public void update(Integer userId, Integer categoryId, Category category) throws ETBadRequestException {
        try {
            jdbcTemplate.update(SQL_UPDATE, category.getTitle(), category.getDescription(), userId, categoryId); //no need for array
        }catch (Exception e){
            System.out.println(e);
            throw new ETBadRequestException("Invalid request");
        }
    }

    private void removeAllCatTransactions(Integer categoryId){
        jdbcTemplate.update(SQL_DELETE_ALL_RELATED_TRANSACTIONS, categoryId);
    }

    @Override
    public void removeById(Integer userId, Integer categoryId) {
        this.removeAllCatTransactions(categoryId);
        jdbcTemplate.update(SQL_DELETE, userId, categoryId);
    }
}
