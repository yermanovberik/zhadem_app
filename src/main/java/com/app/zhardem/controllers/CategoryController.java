package com.app.zhardem.controllers;


import com.app.zhardem.dto.category.CategoryRequestDto;
import com.app.zhardem.dto.category.CategoryResponseDto;
import com.app.zhardem.services.CategoryService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.simpleframework.xml.Path;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryResponseDto getById(@PathVariable long id){
        return categoryService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDto createCategory(@RequestBody @Validated CategoryRequestDto requestDto){
        return categoryService.create(requestDto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CategoryResponseDto updateCategory(@PathVariable long id,@RequestBody @Validated CategoryRequestDto requestDto){
        return categoryService.update(id,requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(
            @PathVariable @Positive(message = "Id must be positive") long id
    ) {
        categoryService.delete(id);
    }
}
