package com.pairlearning.expensetracker.services;

import com.pairlearning.expensetracker.entities.Category;
import com.pairlearning.expensetracker.exceptions.ETBadRequestException;
import com.pairlearning.expensetracker.exceptions.ETResourceNotFoundException;
import com.pairlearning.expensetracker.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public List<Category> fetchAllCategories(Integer userId) {
        return categoryRepository.findAll(userId);
    }

    @Override
    public Category fetchCategoryById(Integer userId, Integer categoryId) throws ETResourceNotFoundException {
        return categoryRepository.findById(userId, categoryId);
    }

    @Override
    public Category addCategory(Integer userId, String title, String description) throws ETBadRequestException {
        int categoryId = categoryRepository.create(userId, title, description);
        return categoryRepository.findById(userId, categoryId);
    }

    @Override
    public void updateCategory(Integer userId, Integer categoryId, Category category) throws ETBadRequestException {
        categoryRepository.update(userId, categoryId, category);
    }

    @Override
    public void removeCategoryWithAllTransactions(Integer userId, Integer categoryId) throws ETResourceNotFoundException {
        this.fetchCategoryById(userId, categoryId); //will throw 404 resource not found exception if no such category found
        categoryRepository.removeById(userId, categoryId);
    }
}
