package com.example.bai8.service;

import com.example.bai8.model.Category;
import com.example.bai8.repository.CategoryRepository;
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
        if (category != null) {
            categoryRepository.save(category);
        }
    }

    public Category getCategoryById(Integer id) {
        if (id != null) {
            return categoryRepository.findById(id).orElse(null);
        }
        return null;
    }

    public void deleteCategory(Integer id) {
        if (id != null) {
            categoryRepository.deleteById(id);
        }
    }
}
