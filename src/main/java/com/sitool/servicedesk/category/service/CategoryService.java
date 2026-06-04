package com.sitool.servicedesk.category.service;

import com.sitool.servicedesk.category.dto.request.CreateCategoryRequest;
import com.sitool.servicedesk.category.dto.request.UpdateCategoryRequest;
import com.sitool.servicedesk.category.dto.responce.CategoryDto;
import com.sitool.servicedesk.category.exceptions.CategoryAlreadyExistException;
import com.sitool.servicedesk.category.exceptions.CategoryNotFoundException;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing ticket categories.
 *
 * <p>Categories are used to classify tickets by type and business purpose.
 * Each category can be assigned to one or more ticket types:
 * Incident, Problem, Request, or Change.</p>
 */
public interface CategoryService {

    /**
     * Creates a new category.
     *
     * @param request the request containing category data
     * @return the created category
     * @throws CategoryAlreadyExistException if a category with the same name already exists
     */
    CategoryDto createCategory(CreateCategoryRequest request);

    /**
     * Updates an existing category.
     *
     * @param categoryId the UUID of the category to update
     * @param request    the request containing updated category data
     * @return the updated category
     * @throws CategoryNotFoundException     if no category with the given ID exists
     * @throws CategoryAlreadyExistException if a category with the new name already exists
     */
    CategoryDto updateCategory(UUID categoryId, UpdateCategoryRequest request);

    /**
     * Deletes a category by its ID.
     *
     * @param categoryId the UUID of the category to delete
     * @throws CategoryNotFoundException if no category with the given ID exists
     */
    void deleteCategory(UUID categoryId);

    /**
     * Returns a category by its ID.
     *
     * @param categoryId the UUID of the category to retrieve
     * @return the category data
     * @throws CategoryNotFoundException if no category with the given ID exists
     */
    CategoryDto getCategory(UUID categoryId);

    /**
     * Returns a list of categories filtered by ticket type.
     *
     * <p>Supported types: INCIDENT, PROBLEM, REQUEST, CHANGE.
     * If no type is provided, all categories are returned.</p>
     *
     * @param type optional ticket type filter (case-insensitive)
     * @return list of matching categories
     */
    List<CategoryDto> getAllCategories(String type);
}
