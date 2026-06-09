package com.sitool.servicedesk.subcategory.service;

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
import com.sitool.servicedesk.sybcategory.service.SubcategoryServiceImpl;
import com.sitool.servicedesk.utils.BaseEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubcategoryServiceImplTests {

    @Mock
    private SubcategoryRepository subcategoryRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private SubcategoryMapper subcategoryMapper;

    @InjectMocks
    private SubcategoryServiceImpl subcategoryService;

    @Test
    @DisplayName("Create subcategory → created successfully")
    void shouldCreateSubcategorySuccessfully() {
        UUID categoryId = UUID.randomUUID();

        CreateSubcategoryRequest request = new CreateSubcategoryRequest(
                "CPU", "Some description", categoryId
        );

        Category category = new Category();
        Subcategory subcategory = new Subcategory();

        SubcategoryDto dto = new SubcategoryDto(UUID.randomUUID(), "CPU", "Some description", categoryId);

        when(subcategoryRepository.existsByNameIgnoreCase("CPU")).thenReturn(false);
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(subcategoryRepository.save(any(Subcategory.class))).thenReturn(subcategory);
        when(subcategoryMapper.sybcategoryToSubcategoryDto(any())).thenReturn(dto);

        SubcategoryDto result = subcategoryService.createSubcategory(request);

        assertEquals("CPU", result.name());
        verify(subcategoryRepository).save(any(Subcategory.class));
    }

    @Test
    @DisplayName("Create subcategory → name already exists → throws exception")
    void shouldThrowExceptionWhenSubcategoryNameAlreadyExists() {
        CreateSubcategoryRequest request = new CreateSubcategoryRequest(
                "CPU", "Some description", UUID.randomUUID()
        );

        when(subcategoryRepository.existsByNameIgnoreCase("CPU")).thenReturn(true);

        assertThrows(SubcategoryAlreadyExistException.class,
                () -> subcategoryService.createSubcategory(request));

        verify(subcategoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("Create subcategory → category not found → throws exception")
    void shouldThrowExceptionWhenCategoryNotFoundOnCreate() {
        UUID categoryId = UUID.randomUUID();

        CreateSubcategoryRequest request = new CreateSubcategoryRequest(
                "CPU", "Some description", categoryId
        );

        when(subcategoryRepository.existsByNameIgnoreCase("CPU")).thenReturn(false);
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
                () -> subcategoryService.createSubcategory(request));

        verify(subcategoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("Update subcategory → updated successfully")
    void shouldUpdateSubcategorySuccessfully() {
        UUID subcategoryId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        Category category = new Category();
        setId(category, categoryId);

        Subcategory existing = new Subcategory();
        existing.setName("CPU");
        existing.setDescription("Old description");
        existing.setCategory(category);

        UpdateSubcategoryRequest request = new UpdateSubcategoryRequest(
                "CPU Updated", "New description", categoryId
        );

        SubcategoryDto dto = new SubcategoryDto(subcategoryId, "CPU Updated", "New description", categoryId);

        when(subcategoryRepository.findById(subcategoryId)).thenReturn(Optional.of(existing));
        when(subcategoryRepository.existsByNameIgnoreCase("CPU Updated")).thenReturn(false);
        when(subcategoryMapper.sybcategoryToSubcategoryDto(any())).thenReturn(dto);

        SubcategoryDto result = subcategoryService.updateSubcategory(subcategoryId, request);

        assertEquals("CPU Updated", result.name());
        verify(subcategoryRepository).save(existing);
    }

    @Test
    @DisplayName("Update subcategory → nothing changed → no save")
    void shouldNotSaveWhenNothingChanged() {
        UUID subcategoryId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        Category category = new Category();
        setId(category, categoryId);

        Subcategory existing = new Subcategory();
        existing.setName("CPU");
        existing.setDescription("Some description");
        existing.setCategory(category);

        UpdateSubcategoryRequest request = new UpdateSubcategoryRequest(
                "CPU", "Some description", categoryId
        );

        when(subcategoryRepository.findById(subcategoryId)).thenReturn(Optional.of(existing));
        when(subcategoryMapper.sybcategoryToSubcategoryDto(any()))
                .thenReturn(new SubcategoryDto(subcategoryId, "CPU", "Some description", categoryId));

        subcategoryService.updateSubcategory(subcategoryId, request);

        verify(subcategoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("Update subcategory → not found → throws exception")
    void shouldThrowExceptionWhenUpdatingNonExistentSubcategory() {
        UUID subcategoryId = UUID.randomUUID();
        UpdateSubcategoryRequest request = new UpdateSubcategoryRequest(
                "CPU", "Some description", UUID.randomUUID()
        );

        when(subcategoryRepository.findById(subcategoryId)).thenReturn(Optional.empty());

        assertThrows(SubcategoryNotFoundException.class,
                () -> subcategoryService.updateSubcategory(subcategoryId, request));
    }

    @Test
    @DisplayName("Update subcategory → category changed → new category not found → throws exception")
    void shouldThrowExceptionWhenNewCategoryNotFound() {
        UUID subcategoryId = UUID.randomUUID();
        UUID oldCategoryId = UUID.randomUUID();
        UUID newCategoryId = UUID.randomUUID();

        Category oldCategory = new Category();
        setId(oldCategory, oldCategoryId);

        Subcategory existing = new Subcategory();
        existing.setName("CPU");
        existing.setDescription("Some description");
        existing.setCategory(oldCategory);

        UpdateSubcategoryRequest request = new UpdateSubcategoryRequest(
                "CPU", "Some description", newCategoryId
        );

        when(subcategoryRepository.findById(subcategoryId)).thenReturn(Optional.of(existing));
        when(categoryRepository.findById(newCategoryId)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
                () -> subcategoryService.updateSubcategory(subcategoryId, request));
    }

    @Test
    @DisplayName("Delete subcategory → deleted successfully")
    void shouldDeleteSubcategorySuccessfully() {
        UUID subcategoryId = UUID.randomUUID();
        Subcategory existing = new Subcategory();

        when(subcategoryRepository.findById(subcategoryId)).thenReturn(Optional.of(existing));

        subcategoryService.deleteSubcategory(subcategoryId);

        verify(subcategoryRepository).delete(existing);
    }

    @Test
    @DisplayName("Delete subcategory → not found → throws exception")
    void shouldThrowExceptionWhenDeletingNonExistentSubcategory() {
        UUID subcategoryId = UUID.randomUUID();

        when(subcategoryRepository.findById(subcategoryId)).thenReturn(Optional.empty());

        assertThrows(SubcategoryNotFoundException.class,
                () -> subcategoryService.deleteSubcategory(subcategoryId));

        verify(subcategoryRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Get subcategory by id → returns subcategory")
    void shouldReturnSubcategoryById() {
        UUID subcategoryId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        Subcategory existing = new Subcategory();
        SubcategoryDto dto = new SubcategoryDto(subcategoryId, "CPU", "Some description", categoryId);

        when(subcategoryRepository.findById(subcategoryId)).thenReturn(Optional.of(existing));
        when(subcategoryMapper.sybcategoryToSubcategoryDto(existing)).thenReturn(dto);

        SubcategoryDto result = subcategoryService.getSubcategoryById(subcategoryId);

        assertEquals(subcategoryId, result.id());
        verify(subcategoryRepository).findById(subcategoryId);
    }

    @Test
    @DisplayName("Get subcategory by id → not found → throws exception")
    void shouldThrowExceptionWhenSubcategoryNotFound() {
        UUID subcategoryId = UUID.randomUUID();

        when(subcategoryRepository.findById(subcategoryId)).thenReturn(Optional.empty());

        assertThrows(SubcategoryNotFoundException.class,
                () -> subcategoryService.getSubcategoryById(subcategoryId));
    }

    @Test
    @DisplayName("Get all subcategories → no categoryId → returns all")
    void shouldReturnAllSubcategoriesWhenNoCategoryId() {
        Subcategory s1 = new Subcategory();
        Subcategory s2 = new Subcategory();

        when(subcategoryRepository.findAll()).thenReturn(List.of(s1, s2));
        when(subcategoryMapper.sybcategoryToSubcategoryDto(any()))
                .thenReturn(new SubcategoryDto(UUID.randomUUID(), "CPU", "", UUID.randomUUID()));

        List<SubcategoryDto> result = subcategoryService.getAllSubcategories(null);

        assertEquals(2, result.size());
        verify(subcategoryRepository).findAll();
    }

    @Test
    @DisplayName("Get all subcategories → categoryId provided → returns filtered")
    void shouldReturnSubcategoriesByCategoryId() {
        UUID categoryId = UUID.randomUUID();
        Subcategory s1 = new Subcategory();

        when(subcategoryRepository.findAllByCategoryId(categoryId)).thenReturn(List.of(s1));
        when(subcategoryMapper.sybcategoryToSubcategoryDto(any()))
                .thenReturn(new SubcategoryDto(UUID.randomUUID(), "CPU", "", categoryId));

        List<SubcategoryDto> result = subcategoryService.getAllSubcategories(categoryId);

        assertEquals(1, result.size());
        verify(subcategoryRepository).findAllByCategoryId(categoryId);
    }

    private void setId(Object entity, UUID id) {
        try {
            Field field = BaseEntity.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(entity, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
