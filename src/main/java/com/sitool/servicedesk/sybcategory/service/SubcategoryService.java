package com.sitool.servicedesk.sybcategory.service;

import com.sitool.servicedesk.sybcategory.dto.request.CreateSubcategoryRequest;
import com.sitool.servicedesk.sybcategory.dto.request.UpdateSubcategoryRequest;
import com.sitool.servicedesk.sybcategory.dto.response.SubcategoryDto;

import java.util.List;
import java.util.UUID;

public interface SubcategoryService {

    SubcategoryDto createSubcategory(CreateSubcategoryRequest request);

    SubcategoryDto updateSubcategory(UUID subcategoryId, UpdateSubcategoryRequest request);

    void deleteSubcategory(UUID subcategoryId);

    SubcategoryDto getSubcategoryById(UUID subcategoryId);

    List<SubcategoryDto> getAllSubcategories(UUID categoryId);
}
