package com.example.bai4.service;

import com.example.bai4.model.Product;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private static List<Product> products = new ArrayList<>();
    private static int nextId = 1;

    static {
        products.add(new Product(nextId++, "SP001", "iPhone 15", 25000000L, "7dd0a093-39a8-4434-99aa-ddf503e1bbf7.jpg", "điện thoại"));
        products.add(new Product(nextId++, "SP002", "Samsung Galaxy S24", 20000000L, "z6575064707417_67349bf72e6cb9e4d73677b49fc1927c.jpg", "điện thoại"));
        products.add(new Product(nextId++, "SP003", "MacBook Pro", 50000000L, "z6857871025550_82e6b510c8b3d692abd4a60869160d49.jpg", "laptop"));
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    public Product getProductById(int id) {
        return products.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void addProduct(Product product) {
        product.setId(nextId++);
        products.add(product);
    }

    public void updateProduct(Product product) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == product.getId()) {
                products.set(i, product);
                break;
            }
        }
    }

    public void deleteProduct(int id) {
        products.removeIf(p -> p.getId() == id);
    }
}
