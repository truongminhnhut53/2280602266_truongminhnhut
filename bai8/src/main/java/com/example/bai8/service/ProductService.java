package com.example.bai8.service;

import com.example.bai8.model.Product;
import com.example.bai8.repository.OrderDetailRepository;
import com.example.bai8.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class ProductService {

    public static final String ERR_DUPLICATE_CODE = "MA_SAN_PHAM_DA_TON_TAI";
    public static final String ERR_PRODUCT_NOT_FOUND = "SAN_PHAM_KHONG_TON_TAI";
    public static final String ERR_PRODUCT_IN_ORDER = "SAN_PHAM_DANG_CO_TRONG_DON_HANG";

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(int id) {
        return productRepository.findById(id).orElse(null);
    }

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("DU_LIEU_SAN_PHAM_KHONG_HOP_LE");
        }

        product.setCode(normalizeCode(product.getCode()));

        if (productRepository.existsByCodeIgnoreCase(product.getCode())) {
            throw new IllegalStateException(ERR_DUPLICATE_CODE);
        }

        try {
            productRepository.save(product);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException(ERR_DUPLICATE_CODE, e);
        }
    }

    public void updateProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("DU_LIEU_SAN_PHAM_KHONG_HOP_LE");
        }

        product.setCode(normalizeCode(product.getCode()));

        if (productRepository.existsByCodeIgnoreCaseAndIdNot(product.getCode(), product.getId())) {
            throw new IllegalStateException(ERR_DUPLICATE_CODE);
        }

        try {
            productRepository.save(product);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException(ERR_DUPLICATE_CODE, e);
        }
    }

    public void deleteProduct(int id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException(ERR_PRODUCT_NOT_FOUND);
        }
        if (orderDetailRepository.existsByProduct_Id(id)) {
            throw new IllegalStateException(ERR_PRODUCT_IN_ORDER);
        }
        productRepository.deleteById(id);
    }

    public List<Product> searchByKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllProducts();
        }
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    public Page<Product> getProductsByPage(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return productRepository.findAll(pageable);
    }

    public Page<Product> searchByKeywordWithPagination(String keyword, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        if (keyword == null || keyword.trim().isEmpty()) {
            return productRepository.findAll(pageable);
        }
        return productRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }

    public List<Product> sortByPriceAscending() {
        return getAllProducts().stream()
                .sorted((p1, p2) -> Long.compare(p1.getPrice(), p2.getPrice()))
                .collect(Collectors.toList());
    }

    public List<Product> sortByPriceDescending() {
        return getAllProducts().stream()
                .sorted((p1, p2) -> Long.compare(p2.getPrice(), p1.getPrice()))
                .collect(Collectors.toList());
    }

    public List<Product> getProductsByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            return getAllProducts();
        }
        return productRepository.findByCategory(category);
    }

    public Page<Product> getProductsByCategoryWithPagination(String category, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        if (category == null || category.trim().isEmpty()) {
            return productRepository.findAll(pageable);
        }
        return productRepository.findByCategory(category, pageable);
    }

    public Page<Product> getProductsWithFilters(String keyword, String category, String sort, int page, int pageSize) {
        int safePage = Math.max(page, 0);
        Pageable pageable = PageRequest.of(safePage, pageSize, buildSort(sort));

        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        boolean hasCategory = category != null && !category.trim().isEmpty();

        if (hasKeyword && hasCategory) {
            return productRepository.findByNameContainingIgnoreCaseAndCategory(
                    keyword.trim(),
                    category.trim(),
                    pageable
            );
        }

        if (hasKeyword) {
            return productRepository.findByNameContainingIgnoreCase(keyword.trim(), pageable);
        }

        if (hasCategory) {
            return productRepository.findByCategory(category.trim(), pageable);
        }

        return productRepository.findAll(pageable);
    }

    private Sort buildSort(String sort) {
        if ("price_asc".equals(sort)) {
            return Sort.by(Sort.Direction.ASC, "price");
        }
        if ("price_desc".equals(sort)) {
            return Sort.by(Sort.Direction.DESC, "price");
        }
        return Sort.by(Sort.Direction.ASC, "id");
    }

    private String normalizeCode(String code) {
        if (code == null) {
            return null;
        }
        return code.trim().toUpperCase(Locale.ROOT);
    }
}
