package com.example.bai4.service;

import org.springframework.stereotype.Service;
import com.example.bai4.model.Category;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    private List<Category> categories = new ArrayList<>();

    public CategoryService() {
        categories.add(new Category(1, "điện thoại"));
        categories.add(new Category(2, "laptop"));
    }

    public List<Category> getAllCategories() {
        return categories;
    }

    public Category getCategoryById(int id) {
        return categories.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
