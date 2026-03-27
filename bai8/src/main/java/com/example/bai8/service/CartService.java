package com.example.bai8.service;

import com.example.bai8.model.CartItem;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@SessionScope
public class CartService implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<CartItem> cart = new ArrayList<>();
    
    // Câu 5: Thêm sản phẩm vào giỏ hàng
    public void addToCart(int productId, String name, Long price, String image) {
        Optional<CartItem> existingItem = cart.stream()
                .filter(item -> item.getProductId() == productId)
                .findFirst();
        
        if (existingItem.isPresent()) {
            // Nếu đã có, tăng quantity
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + 1);
        } else {
            // Nếu chưa có, thêm mới
            CartItem newItem = new CartItem(productId, name, price, image);
            cart.add(newItem);
        }
    }
    
    public void addToCartWithQuantity(int productId, String name, Long price, String image, int quantity) {
        Optional<CartItem> existingItem = cart.stream()
                .filter(item -> item.getProductId() == productId)
                .findFirst();
        
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem(productId, name, price, image);
            newItem.setQuantity(quantity);
            cart.add(newItem);
        }
    }
    
    // Câu 6: Lấy danh sách giỏ hàng
    public List<CartItem> getCartItems() {
        return cart;
    }
    
    // Lấy số lượng items trong giỏ
    public int getCartSize() {
        return cart.size();
    }
    
    // Lấy tổng tiền giỏ hàng
    public Long getTotalPrice() {
        return cart.stream()
                .mapToLong(CartItem::getTotal)
                .sum();
    }
    
    // Cập nhật quantity
    public void updateQuantity(int productId, int quantity) {
        Optional<CartItem> item = cart.stream()
                .filter(i -> i.getProductId() == productId)
                .findFirst();
        
        if (item.isPresent()) {
            if (quantity > 0) {
                item.get().setQuantity(quantity);
            } else {
                removeFromCart(productId);
            }
        }
    }
    
    // Xóa sản phẩm khỏi giỏ
    public void removeFromCart(int productId) {
        cart.removeIf(item -> item.getProductId() == productId);
    }
    
    // Xóa toàn bộ giỏ hàng
    public void clearCart() {
        cart.clear();
    }
}
