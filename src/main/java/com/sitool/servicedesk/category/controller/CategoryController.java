package com.sitool.servicedesk.category.controller;

import com.sitool.servicedesk.category.dto.request.CreateCategoryRequest;
import com.sitool.servicedesk.category.dto.request.UpdateCategoryRequest;
import com.sitool.servicedesk.category.dto.responce.CategoryDto;
import com.sitool.servicedesk.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST controller that delegates category operations to the CategoryService.
 */
@RestController
@RequiredArgsConstructor
public class CategoryController implements CategoryApi {

    private final CategoryService categoryService;

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public CategoryDto createCategory(CreateCategoryRequest request) {
        return categoryService.createCategory(request);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public CategoryDto updateCategory(UUID categoryId, UpdateCategoryRequest request) {
        return categoryService.updateCategory(categoryId, request);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public void deleteCategory(UUID categoryId) {
        categoryService.deleteCategory(categoryId);
    }

    @Override
    public CategoryDto getCategory(UUID categoryId) {
        return categoryService.getCategory(categoryId);
    }

    @Override
    public List<CategoryDto> getAllCategories(String type) {
        return categoryService.getAllCategories(type);
    }
}
