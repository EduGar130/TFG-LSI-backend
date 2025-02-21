package com.tfg.mapper;

import com.tfg.dto.CategoryDTO;
import com.tfg.entity.Category;

public class CategoryMapper {

    public static CategoryDTO toDto(Category category) {
        if (category == null) return null;
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        return dto;
    }

    public static Category toEntity(CategoryDTO category) {
        if (category == null) return null;
        Category entity = new Category();
        entity.setId(category.getId());
        entity.setName(category.getName());
        entity.setDescription(category.getDescription());
        return entity;
    }
}
