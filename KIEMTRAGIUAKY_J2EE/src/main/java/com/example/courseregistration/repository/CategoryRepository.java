package com.example.courseregistration.repository;
import com.example.courseregistration.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
