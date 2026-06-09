package com.sitool.servicedesk.sybcategory.service;

import com.sitool.servicedesk.sybcategory.dto.request.CreateSubcategoryRequest;
import com.sitool.servicedesk.sybcategory.dto.request.UpdateSubcategoryRequest;
import com.sitool.servicedesk.sybcategory.dto.response.SubcategoryDto;
import com.sitool.servicedesk.sybcategory.exceptions.SubcategoryAlreadyExistException;
import com.sitool.servicedesk.sybcategory.exceptions.SubcategoryNotFoundException;
import com.sitool.servicedesk.category.exceptions.CategoryNotFoundException;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing ticket subcategories.
 *
 * <p>Subcategories provide a second level of classification for tickets
 * within a parent category.</p>
 */
public interface SubcategoryService {
    /**
     * Creates a new subcategory.
     *
     * @param request the request containing subcategory data
     * @return the created subcategory
     * @throws CategoryNotFoundException         if the specified parent category does not exist
     * @throws SubcategoryAlreadyExistException  if a subcategory with the same name already exists in the category
     */
    SubcategoryDto createSubcategory(CreateSubcategoryRequest request);

    /**
     * Updates an existing subcategory.
     *
     * @param subcategoryId the UUID of the subcategory to update
     * @param request       the request containing updated subcategory data
     * @return the updated subcategory
     * @throws SubcategoryNotFoundException      if no subcategory with the given ID exists
     * @throws SubcategoryAlreadyExistException  if a subcategory with the new name already exists in the category
     */
    SubcategoryDto updateSubcategory(UUID subcategoryId, UpdateSubcategoryRequest request);

    /**
     * Deletes a subcategory by its ID.
     *
     * @param subcategoryId the UUID of the subcategory to delete
     * @throws SubcategoryNotFoundException if no subcategory with the given ID exists
     */
    void deleteSubcategory(UUID subcategoryId);

    /**
     * Returns a subcategory by its ID.
     *
     * @param subcategoryId the UUID of the subcategory to retrieve
     * @return the subcategory data
     * @throws SubcategoryNotFoundException if no subcategory with the given ID exists
     */
    SubcategoryDto getSubcategoryById(UUID subcategoryId);

    /**
     * Returns a list of subcategories optionally filtered by parent category.
     *
     * <p>If categoryId is null, all subcategories are returned.
     * If categoryId is provided, only subcategories belonging to that category are returned.</p>
     *
     * @param categoryId optional UUID of the parent category
     * @return list of matching subcategories
     */
    List<SubcategoryDto> getAllSubcategories(UUID categoryId);
}
