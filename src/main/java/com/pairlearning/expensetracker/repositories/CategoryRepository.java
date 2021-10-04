package com.pairlearning.expensetracker.repositories;

import com.pairlearning.expensetracker.entities.Category;
import com.pairlearning.expensetracker.exceptions.ETBadRequestException;
import com.pairlearning.expensetracker.exceptions.ETResourceNotFoundException;

import java.util.List;

public interface CategoryRepository {

    List<Category> findAll(Integer userId) throws ETResourceNotFoundException;

    Category findById(Integer userId, Integer categoryId) throws ETResourceNotFoundException;

    Integer create(Integer userId, String title, String description) throws ETBadRequestException;

    void update(Integer userId, Integer categoryId, Category category) throws ETBadRequestException;

    void removeById(Integer userId, Integer categoryId);

}
