package com.sitool.servicedesk.sybcategory.service;

import com.sitool.servicedesk.category.entity.Category;
import com.sitool.servicedesk.category.exceptions.CategoryNotFoundException;
import com.sitool.servicedesk.category.repository.CategoryRepository;
import com.sitool.servicedesk.sybcategory.dto.request.CreateSubcategoryRequest;
import com.sitool.servicedesk.sybcategory.dto.request.UpdateSubcategoryRequest;
import com.sitool.servicedesk.sybcategory.dto.response.SubcategoryDto;
import com.sitool.servicedesk.sybcategory.entity.Subcategory;
import com.sitool.servicedesk.sybcategory.exceptions.SubcategoryAlreadyExistException;
import com.sitool.servicedesk.sybcategory.exceptions.SubcategoryNotFoundException;
import com.sitool.servicedesk.sybcategory.mapper.SubcategoryMapper;
import com.sitool.servicedesk.sybcategory.repository.SubcategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Implementation of {@link SubcategoryService} for managing ticket subcategories.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SubcategoryServiceImpl implements SubcategoryService{

    private final SubcategoryRepository subcategoryRepository;
    private final CategoryRepository categoryRepository;
    private final SubcategoryMapper subcategoryMapper;

    /**
     * Creates a new subcategory.
     *
     * <p>Normalizes the subcategory name by trimming whitespace before saving.</p>
     *
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public SubcategoryDto createSubcategory(CreateSubcategoryRequest request) {
        String normalizedName = request.name().trim();
        if (subcategoryRepository.existsByNameIgnoreCase(normalizedName)) {
            log.info("createSubcategory: Subcategory with name {} already exists", normalizedName);
            throw new SubcategoryAlreadyExistException();
        }

        Subcategory subcategory = new Subcategory();
        subcategory.setName(normalizedName);
        subcategory.setDescription(request.description());
        Category category = categoryRepository.findById(request.categoryId()).orElseThrow(() -> {
            log.error("createSubcategory: Category {} did not found", request.categoryId());
            return new CategoryNotFoundException();
        });
        subcategory.setCategory(category);
        subcategoryRepository.save(subcategory);
        return subcategoryMapper.sybcategoryToSubcategoryDto(subcategory);
    }

    /**
     * Updates an existing subcategory.
     *
     * <p>Only modified fields are persisted to avoid unnecessary database calls.
     * If the parent category is changed, the new category is validated before updating.</p>
     *
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public SubcategoryDto updateSubcategory(UUID subcategoryId, UpdateSubcategoryRequest request) {
        boolean updated = false;
        String normalizedName = request.name().trim();

        Subcategory currentSubcategory = subcategoryRepository.findById(subcategoryId).orElseThrow(() -> {
            log.error("updateSubcategory: Subcategory {} did not found", request.categoryId());
            return new SubcategoryNotFoundException();
        });

        if (!currentSubcategory.getName().equalsIgnoreCase(normalizedName) && subcategoryRepository.existsByNameIgnoreCase(normalizedName)) {
            log.info("updateSubcategory: Subcategory with name {} already exists", normalizedName);
            throw new SubcategoryAlreadyExistException();
        }

        if (!currentSubcategory.getName().equals(normalizedName)) {
            updated = true;
            currentSubcategory.setName(normalizedName);
        }

        if (!Objects.equals(currentSubcategory.getDescription(), request.description())) {
            updated = true;
            currentSubcategory.setDescription(request.description());
        }

        if (!currentSubcategory.getCategory().getId().equals(request.categoryId())) {
            Category newCategory = categoryRepository.findById(request.categoryId()).orElseThrow(() -> {
                log.error("updateSubcategory: Category {} did not found", request.categoryId());
                return new CategoryNotFoundException();
            });
            updated = true;
            currentSubcategory.setCategory(newCategory);
        }

        if (updated) {
            subcategoryRepository.save(currentSubcategory);
        }

        return subcategoryMapper.sybcategoryToSubcategoryDto(currentSubcategory);
    }

    /**
     * Deletes a subcategory by its ID.
     *
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteSubcategory(UUID subcategoryId) {
        Subcategory currentSubcategory = subcategoryRepository.findById(subcategoryId).orElseThrow(() -> {
            log.error("deleteSubcategory: Subcategory {} did not found", subcategoryId);
            return new SubcategoryNotFoundException();
        });
        subcategoryRepository.delete(currentSubcategory);
    }

    /**
     * Returns a subcategory by its ID.
     *
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public SubcategoryDto getSubcategoryById(UUID subcategoryId) {
        Subcategory currentSubcategory = subcategoryRepository.findById(subcategoryId).orElseThrow(() -> {
            log.error("getSubcategory: Subcategory {} did not found", subcategoryId);
            return new SubcategoryNotFoundException();
        });
        return subcategoryMapper.sybcategoryToSubcategoryDto(currentSubcategory);
    }

    /**
     * Returns subcategories optionally filtered by parent category.
     *
     * <p>If categoryId is null, all subcategories are returned.</p>
     *
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<SubcategoryDto> getAllSubcategories(UUID categoryId) {
        if (categoryId == null) {
            return subcategoryRepository.findAll()
                    .stream()
                    .map(subcategoryMapper::sybcategoryToSubcategoryDto)
                    .toList();
        }

        return subcategoryRepository.findAllByCategoryId(categoryId)
                .stream()
                .map(subcategoryMapper::sybcategoryToSubcategoryDto)
                .toList();
    }
}
