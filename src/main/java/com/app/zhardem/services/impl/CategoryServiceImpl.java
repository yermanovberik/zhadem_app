package com.app.zhardem.services.impl;

import com.app.zhardem.dto.category.CategoryRequestDto;
import com.app.zhardem.dto.category.CategoryResponseDto;
import com.app.zhardem.exceptions.entity.EntityAlreadyExistsException;
import com.app.zhardem.exceptions.entity.EntityNotFoundException;
import com.app.zhardem.models.Category;
import com.app.zhardem.repositories.CategoryRepository;
import com.app.zhardem.services.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    @Override
    public CategoryResponseDto getById(long id) {
        Category category = getEntityById(id);
        CategoryResponseDto responseDto = CategoryResponseDto.builder()
                .id(id)
                .name(category.getName())
                .build();
        return responseDto;
    }

    @Override
    public CategoryResponseDto create(CategoryRequestDto requestDto) {
        throwExceptionIfCategoryExists(requestDto.name());

        Category category = Category.builder()
                .name(requestDto.name())
                .build();

        CategoryResponseDto responseDto = CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
        return responseDto;
    }

    @Override
    public CategoryResponseDto update(long id, CategoryRequestDto requestDto) {

        Category category = getEntityById(id);

        String oldName = category.getName();
        String newName = requestDto.name();

        if(!Objects.equals(oldName,newName)){
            throwExceptionIfCategoryExists(newName);
            category.setName(newName);
            categoryRepository.save(category);
        }

        CategoryResponseDto responseDto = CategoryResponseDto.builder()
                .id(id)
                .name(requestDto.name())
                .build();
        return responseDto;
    }

    private void throwExceptionIfCategoryExists(String categoryName) {
        categoryRepository.findByName(categoryName)
                .ifPresent(foundCategory -> {
                    throw new EntityAlreadyExistsException(
                            "Category with the name " + foundCategory.getName() + " already exists"
                    );
                });
    }

    @Override
    @Transactional
    public void delete(long id) {
        Category category = getEntityById(id);
        categoryRepository.delete(category);
    }

    @Override
    public Category getEntityById(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with id " + id + " does not exists"));
    }
}
