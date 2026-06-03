package com.sitool.servicedesk.category.controller;

import com.sitool.servicedesk.category.dto.request.CreateCategoryRequest;
import com.sitool.servicedesk.category.dto.request.UpdateCategoryRequest;
import com.sitool.servicedesk.category.dto.responce.CategoryDto;
import com.sitool.servicedesk.category.entity.Category;
import com.sitool.servicedesk.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST controller that delegates category operations to the CategoryService.
 */
@RestController
@RequiredArgsConstructor
public class CategoryController implements CategoryApi {
    private final CategoryRepository categoryRepository;


    @Override
    public CategoryDto createCategory(CreateCategoryRequest request) {
        return null;
    }

    @Override
    public CategoryDto updateCategory(UUID categoryId, UpdateCategoryRequest request) {
        return null;
    }

    @Override
    public void deleteCategory(UUID categoryId) {

    }

    @Override
    public CategoryDto getCategory(UUID categoryId) {
        return null;
    }

    @Override
    public List<Category> getAllCategories() {
        return List.of();
    }
}
