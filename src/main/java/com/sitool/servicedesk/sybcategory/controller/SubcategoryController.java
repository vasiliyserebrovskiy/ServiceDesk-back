package com.sitool.servicedesk.sybcategory.controller;

import com.sitool.servicedesk.sybcategory.dto.request.CreateSubcategoryRequest;
import com.sitool.servicedesk.sybcategory.dto.request.UpdateSubcategoryRequest;
import com.sitool.servicedesk.sybcategory.dto.response.SubcategoryDto;
import com.sitool.servicedesk.sybcategory.service.SubcategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST controller that delegates subcategory operations to the SubcategoryService.
 */
@RestController
@RequiredArgsConstructor
public class SubcategoryController implements SubcategoryApi {

    private final SubcategoryService subcategoryService;

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public SubcategoryDto createSubcategory(CreateSubcategoryRequest request) {
        return subcategoryService.createSubcategory(request);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public SubcategoryDto updateSubcategory(UUID subcategoryId, UpdateSubcategoryRequest request) {
        return subcategoryService.updateSubcategory(subcategoryId,request);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public void deleteSubcategory(UUID subcategoryId) {
        subcategoryService.deleteSubcategory(subcategoryId);
    }

    @Override
    public SubcategoryDto getSubcategoryById(UUID subcategoryId) {
        return subcategoryService.getSubcategoryById(subcategoryId);
    }

    @Override
    public List<SubcategoryDto> getAllSubcategories(UUID categoryId) {
        return subcategoryService.getAllSubcategories(categoryId);
    }
}
