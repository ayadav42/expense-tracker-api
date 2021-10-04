package com.pairlearning.expensetracker.services;

import com.pairlearning.expensetracker.entities.Category;
import com.pairlearning.expensetracker.exceptions.ETBadRequestException;
import com.pairlearning.expensetracker.exceptions.ETResourceNotFoundException;

import java.util.List;

public interface CategoryService {

    List<Category> fetchAllCategories(Integer userId);

    Category fetchCategoryById(Integer userId, Integer categoryId) throws ETResourceNotFoundException;

    Category addCategory(Integer userId, String title, String description) throws ETBadRequestException;

    void updateCategory(Integer userId, Integer categoryId, Category category) throws ETBadRequestException;

    void removeCategoryWithAllTransactions(Integer userId, Integer categoryId) throws ETResourceNotFoundException; //we remove transactions first and then category => cascade delete

}
