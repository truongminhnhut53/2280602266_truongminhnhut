package com.example.bai4.service;

import com.example.bai4.model.Product;
import com.example.bai4.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(int id) {
        return productRepository.findById(id).orElse(null);
    }

    public void addProduct(Product product) {
        // Khi thêm mới, id sẽ được sinh tự động (IDENTITY)
        productRepository.save(product);
    }

    public void updateProduct(Product product) {
        // save() với id đã tồn tại sẽ thực hiện update
        productRepository.save(product);
    }

    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }
}
