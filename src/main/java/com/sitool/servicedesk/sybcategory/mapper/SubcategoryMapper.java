package com.sitool.servicedesk.sybcategory.mapper;

import com.sitool.servicedesk.sybcategory.dto.response.SubcategoryDto;
import com.sitool.servicedesk.sybcategory.entity.Subcategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubcategoryMapper {
    @Mapping(target = "categoryId", expression = "java(subcategory.getCategory().getId())")
    SubcategoryDto sybcategoryToSubcategoryDto(Subcategory subcategory);
}
