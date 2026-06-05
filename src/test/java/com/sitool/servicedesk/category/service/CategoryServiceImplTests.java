package com.sitool.servicedesk.category.service;

import com.sitool.servicedesk.category.dto.request.CreateCategoryRequest;
import com.sitool.servicedesk.category.dto.request.UpdateCategoryRequest;
import com.sitool.servicedesk.category.dto.responce.CategoryDto;
import com.sitool.servicedesk.category.entity.Category;
import com.sitool.servicedesk.category.exceptions.CategoryAlreadyExistException;
import com.sitool.servicedesk.category.exceptions.CategoryNotFoundException;
import com.sitool.servicedesk.category.mapper.CategoryMapper;
import com.sitool.servicedesk.category.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTests {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Create category → created successfully")
    void shouldCreateCategorySuccessfully() {
        CreateCategoryRequest request = new CreateCategoryRequest(
                "Hardware", "Some description", true, false, false, false
        );

        when(categoryRepository.existsByNameIgnoreCase("Hardware")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenAnswer(i -> i.getArgument(0));
        when(categoryMapper.categoryToCategoryDto(any(Category.class)))
                .thenReturn(new CategoryDto(UUID.randomUUID(), "Hardware", "Some description", true, false, false, false));

        CategoryDto result = categoryService.createCategory(request);

        assertEquals("Hardware", result.name());
        assertTrue(result.isIncident());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    @DisplayName("Create category → name already exists → throws exception")
    void shouldThrowExceptionWhenCategoryNameAlreadyExists() {
        CreateCategoryRequest request = new CreateCategoryRequest(
                "Hardware", "Some description", true, false, false, false
        );

        when(categoryRepository.existsByNameIgnoreCase("Hardware")).thenReturn(true);

        assertThrows(CategoryAlreadyExistException.class,
                () -> categoryService.createCategory(request));

        verify(categoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("Update category → updated successfully")
    void shouldUpdateCategorySuccessfully() {
        UUID categoryId = UUID.randomUUID();

        Category existing = new Category();
        existing.setName("Hardware");
        existing.setDescription("Old description");
        existing.setIsIncident(true);
        existing.setIsProblem(false);
        existing.setIsRequest(false);
        existing.setIsChange(false);

        UpdateCategoryRequest request = new UpdateCategoryRequest(
                "Hardware Updated", "New description", true, false, false, false
        );

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existing));
        when(categoryRepository.existsByNameIgnoreCase("Hardware Updated")).thenReturn(false);
        when(categoryMapper.categoryToCategoryDto(any()))
                .thenReturn(new CategoryDto(categoryId, "Hardware Updated", "New description", true, false, false, false));

        CategoryDto result = categoryService.updateCategory(categoryId, request);

        assertEquals("Hardware Updated", result.name());
        verify(categoryRepository).save(existing);
    }

    @Test
    @DisplayName("Update category → nothing changed → no save")
    void shouldNotSaveWhenNothingChanged() {
        UUID categoryId = UUID.randomUUID();

        Category existing = new Category();
        existing.setName("Hardware");
        existing.setDescription("Some description");
        existing.setIsIncident(true);
        existing.setIsProblem(false);
        existing.setIsRequest(false);
        existing.setIsChange(false);

        UpdateCategoryRequest request = new UpdateCategoryRequest(
                "Hardware", "Some description", true, false, false, false
        );

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existing));
        when(categoryMapper.categoryToCategoryDto(any()))
                .thenReturn(new CategoryDto(categoryId, "Hardware", "Some description", true, false, false, false));

        categoryService.updateCategory(categoryId, request);

        verify(categoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("Update category → not found → throws exception")
    void shouldThrowExceptionWhenUpdatingNonExistentCategory() {
        UUID categoryId = UUID.randomUUID();
        UpdateCategoryRequest request = new UpdateCategoryRequest(
                "Hardware", "Some description", true, false, false, false
        );

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
                () -> categoryService.updateCategory(categoryId, request));
    }

    @Test
    @DisplayName("Update category → new name already exists → throws exception")
    void shouldThrowExceptionWhenNewNameAlreadyExists() {
        UUID categoryId = UUID.randomUUID();

        Category existing = new Category();
        existing.setName("Hardware");
        existing.setDescription("Some description");
        existing.setIsIncident(true);
        existing.setIsProblem(false);
        existing.setIsRequest(false);
        existing.setIsChange(false);

        UpdateCategoryRequest request = new UpdateCategoryRequest(
                "Network", "Some description", true, false, false, false
        );

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existing));
        when(categoryRepository.existsByNameIgnoreCase("Network")).thenReturn(true);

        assertThrows(CategoryAlreadyExistException.class,
                () -> categoryService.updateCategory(categoryId, request));

        verify(categoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("Delete category → deleted successfully")
    void shouldDeleteCategorySuccessfully() {
        UUID categoryId = UUID.randomUUID();
        Category existing = new Category();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existing));

        categoryService.deleteCategory(categoryId);

        verify(categoryRepository).delete(existing);
    }

    @Test
    @DisplayName("Delete category → not found → throws exception")
    void shouldThrowExceptionWhenDeletingNonExistentCategory() {
        UUID categoryId = UUID.randomUUID();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
                () -> categoryService.deleteCategory(categoryId));

        verify(categoryRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Get category by id → returns category")
    void shouldReturnCategoryById() {
        UUID categoryId = UUID.randomUUID();
        Category existing = new Category();

        CategoryDto dto = new CategoryDto(categoryId, "Hardware", "Some description", true, false, false, false);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existing));
        when(categoryMapper.categoryToCategoryDto(existing)).thenReturn(dto);

        CategoryDto result = categoryService.getCategory(categoryId);

        assertEquals(categoryId, result.id());
        verify(categoryRepository).findById(categoryId);
    }

    @Test
    @DisplayName("Get category by id → not found → throws exception")
    void shouldThrowExceptionWhenCategoryNotFound() {
        UUID categoryId = UUID.randomUUID();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
                () -> categoryService.getCategory(categoryId));
    }

    @Test
    @DisplayName("Get all categories → no type → returns all")
    void shouldReturnAllCategoriesWhenNoType() {
        Category c1 = new Category();
        Category c2 = new Category();

        when(categoryRepository.findAll()).thenReturn(List.of(c1, c2));
        when(categoryMapper.categoryToCategoryDto(any())).thenReturn(
                new CategoryDto(UUID.randomUUID(), "Hardware", "", true, false, false, false)
        );

        List<CategoryDto> result = categoryService.getAllCategories(null);

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Get all categories → type INCIDENT → returns incident categories")
    void shouldReturnIncidentCategories() {
        Category c1 = new Category();

        when(categoryRepository.findAllByIsIncidentTrue()).thenReturn(List.of(c1));
        when(categoryMapper.categoryToCategoryDto(any())).thenReturn(
                new CategoryDto(UUID.randomUUID(), "Hardware", "", true, false, false, false)
        );

        List<CategoryDto> result = categoryService.getAllCategories("INCIDENT");

        assertEquals(1, result.size());
        verify(categoryRepository).findAllByIsIncidentTrue();
    }

    @Test
    @DisplayName("Get all categories → unknown type → returns all")
    void shouldReturnAllCategoriesWhenUnknownType() {
        when(categoryRepository.findAll()).thenReturn(List.of());

        List<CategoryDto> result = categoryService.getAllCategories("UNKNOWN");

        verify(categoryRepository).findAll();
    }
}
