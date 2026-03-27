package com.example.bai8.repository;

import com.example.bai8.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    // Tim kiem theo ten (keyword)
    List<Product> findByNameContainingIgnoreCase(String keyword);

    // Tim kiem theo ten voi phan trang
    Page<Product> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCaseAndCategory(String keyword, String category, Pageable pageable);

    // Loc theo category
    List<Product> findByCategory(String category);

    // Loc theo category voi phan trang
    Page<Product> findByCategory(String category, Pageable pageable);

    // Kiem tra trung ma san pham (khong phan biet hoa thuong)
    boolean existsByCodeIgnoreCase(String code);

    // Kiem tra trung ma khi cap nhat (bo qua ban ghi hien tai)
    boolean existsByCodeIgnoreCaseAndIdNot(String code, int id);

    // Phan trang
    @org.springframework.lang.NonNull
    Page<Product> findAll(@org.springframework.lang.NonNull Pageable pageable);
}
