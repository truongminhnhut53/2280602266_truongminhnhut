package com.example.bai6.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bai6.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}

