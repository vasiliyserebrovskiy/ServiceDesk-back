package com.sitool.servicedesk.category.service;

import com.sitool.servicedesk.category.dto.request.CreateCategoryRequest;
import com.sitool.servicedesk.category.dto.request.UpdateCategoryRequest;
import com.sitool.servicedesk.category.dto.responce.CategoryDto;
import com.sitool.servicedesk.category.entity.Category;
import com.sitool.servicedesk.category.exceptions.CategoryAlreadyExistException;
import com.sitool.servicedesk.category.exceptions.CategoryNotFoundException;
import com.sitool.servicedesk.category.mapper.CategoryMapper;
import com.sitool.servicedesk.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Implementation of {@link CategoryService} for managing ticket categories.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    /**
     * Creates a new category.
     *
     * <p>Normalizes the category name by trimming whitespace before saving.</p>
     *
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public CategoryDto createCategory(CreateCategoryRequest request) {
        String normalizeName = request.name().trim();
        if (categoryRepository.existsByNameIgnoreCase(normalizeName)) {
            log.info("Create Category: Category already exists with the name {}", normalizeName);
            throw new CategoryAlreadyExistException();
        }

        Category category = new Category();
        category.setName(normalizeName);
        category.setDescription(request.description());
        category.setIsIncident(request.isIncident());
        category.setIsProblem(request.isProblem());
        category.setIsRequest(request.isRequest());
        category.setIsChange(request.isChange());

        categoryRepository.save(category);

        return categoryMapper.categoryToCategoryDto(category);
    }

    /**
     * Updates an existing category.
     *
     * <p>Only modified fields are persisted to avoid unnecessary database calls.</p>
     *
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public CategoryDto updateCategory(UUID categoryId, UpdateCategoryRequest request) {
        boolean updated = false;
        String normalizedName = request.name().trim();

        Category currentCategory = categoryRepository.findById(categoryId).orElseThrow(() -> {
            log.error("Update Category: Category {} did not found", categoryId);
            return new CategoryNotFoundException();
        });

        if(!currentCategory.getName().equalsIgnoreCase(normalizedName)
                && categoryRepository.existsByNameIgnoreCase(normalizedName)) {
            log.info("Update Category: Category already exists with the name {}", normalizedName);
            throw new CategoryAlreadyExistException();
        }

        if (!currentCategory.getName().equals(normalizedName)) {
            currentCategory.setName(normalizedName);
            updated = true;
        }

        if (!Objects.equals(currentCategory.getDescription(), request.description())) {
            currentCategory.setDescription(request.description());
            updated = true;
        }

        if (currentCategory.getIsIncident() != request.isIncident()) {
            currentCategory.setIsIncident(request.isIncident());
            updated = true;
        }

        if (currentCategory.getIsProblem() != request.isProblem()) {
            currentCategory.setIsProblem(request.isProblem());
            updated = true;
        }

        if (currentCategory.getIsRequest() != request.isRequest()) {
            currentCategory.setIsRequest(request.isRequest());
            updated = true;
        }

        if (currentCategory.getIsChange() != request.isChange()) {
            currentCategory.setIsChange(request.isChange());
            updated = true;
        }

        if (updated) {
            categoryRepository.save(currentCategory);
        }
        return categoryMapper.categoryToCategoryDto(currentCategory);
    }

    /**
     * Deletes a category by its ID.
     *
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteCategory(UUID categoryId) {
        Category currentCategory = categoryRepository.findById(categoryId).orElseThrow(() -> {
            log.error("Delete Category: Category {} did not found", categoryId);
            return new CategoryNotFoundException();
        });

        categoryRepository.delete(currentCategory);
    }

    /**
     * Returns a category by its ID.
     *
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategory(UUID categoryId) {
        Category currentCategory = categoryRepository.findById(categoryId).orElseThrow(() -> {
            log.error("Get Category: Category {} did not found", categoryId);
            return new CategoryNotFoundException();
        });
        return categoryMapper.categoryToCategoryDto(currentCategory);
    }

    /**
     * Returns categories filtered by ticket type.
     *
     * <p>If type is null or blank, all categories are returned.
     * Unrecognized type values fall back to returning all categories.</p>
     *
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories(String type) {

        if (type == null || type.isBlank()) {
            return categoryRepository.findAll()
                    .stream()
                    .map(categoryMapper::categoryToCategoryDto)
                    .toList();
        }

        List<Category> categories = switch (type.toUpperCase()) {
            case "INCIDENT" -> categoryRepository.findAllByIsIncidentTrue();
            case "PROBLEM" -> categoryRepository.findAllByIsProblemTrue();
            case "REQUEST" -> categoryRepository.findAllByIsRequestTrue();
            case "CHANGE" -> categoryRepository.findAllByIsChangeTrue();
            default -> categoryRepository.findAll();
        };

        return categories.stream()
                .map(categoryMapper::categoryToCategoryDto)
                .toList();
    }
}
