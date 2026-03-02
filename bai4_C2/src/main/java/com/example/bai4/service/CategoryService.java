package com.example.bai4.service;

import com.example.bai4.model.Category;
import com.example.bai4.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Lấy TẤT CẢ danh mục đúng theo dữ liệu trong bảng categories (Heidi)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }

    public Category getCategoryById(Integer id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public void deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
    }
}
