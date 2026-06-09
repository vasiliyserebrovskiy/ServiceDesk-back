package com.sitool.servicedesk.sybcategory.mapper;

import com.sitool.servicedesk.sybcategory.dto.response.SubcategoryDto;
import com.sitool.servicedesk.sybcategory.entity.Subcategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubcategoryMapper {
    SubcategoryDto sybcategoryToSubcategoryDto(Subcategory subcategory);
}
