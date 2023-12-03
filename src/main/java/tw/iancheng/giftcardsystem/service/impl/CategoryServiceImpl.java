package tw.iancheng.giftcardsystem.service.impl;

import org.springframework.stereotype.Service;
import tw.iancheng.giftcardsystem.model.Category;
import tw.iancheng.giftcardsystem.repository.CategoryRepository;
import tw.iancheng.giftcardsystem.service.CategoryService;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

}
