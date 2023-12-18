package tw.iancheng.giftcardsystem.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tw.iancheng.giftcardsystem.dto.category.CategoryCreateRequest;
import tw.iancheng.giftcardsystem.model.Category;
import tw.iancheng.giftcardsystem.service.CategoryService;

import java.util.List;

@Validated
@RestController
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> categories = categoryService.getCategories();

        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }

    @PostMapping("/categories")
    public ResponseEntity<Category> createCategory(@RequestBody @Validated CategoryCreateRequest categoryCreateRequest) {
        Category category = categoryService.createCategory(categoryCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }
}
