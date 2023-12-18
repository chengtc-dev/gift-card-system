package tw.iancheng.giftcardsystem.service;

import tw.iancheng.giftcardsystem.dto.category.CategoryCreateRequest;
import tw.iancheng.giftcardsystem.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getCategories();

    Category createCategory(CategoryCreateRequest categoryCreateRequest);
}
