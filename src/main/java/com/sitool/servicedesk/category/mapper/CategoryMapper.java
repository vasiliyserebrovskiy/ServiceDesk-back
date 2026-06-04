package com.sitool.servicedesk.category.mapper;

import com.sitool.servicedesk.category.dto.responce.CategoryDto;
import com.sitool.servicedesk.category.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto categoryToCategoryDto(Category category);
}
