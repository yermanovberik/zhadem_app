package com.app.zhardem.services;

import com.app.zhardem.dto.category.CategoryRequestDto;
import com.app.zhardem.dto.category.CategoryResponseDto;
import com.app.zhardem.exceptions.entity.EntityNotFoundException;
import com.app.zhardem.models.Category;

public interface CategoryService extends CrudService<Category, CategoryRequestDto, CategoryResponseDto> {
     Category getEntityByName(String name);
}
