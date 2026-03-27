package com.example.bai8.service;

import com.example.bai8.model.Order;
import com.example.bai8.model.OrderDetail;
import com.example.bai8.model.CartItem;
import com.example.bai8.model.Product;
import com.example.bai8.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private ProductService productService;
    
    // Câu 7: Tạo Order từ giỏ hàng
    @Transactional
    public Order createOrderFromCart(List<CartItem> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            return null;
        }
        
        Order order = new Order();
        order.setOrderDetails(new ArrayList<>());
        BigDecimal totalAmount = BigDecimal.ZERO;
        
        for (CartItem cartItem : cartItems) {
            Product product = productService.getProductById(cartItem.getProductId());
            if (product != null) {
                BigDecimal price = BigDecimal.valueOf(product.getPrice());
                int quantity = cartItem.getQuantity();
                
                OrderDetail orderDetail = new OrderDetail(order, product, quantity, price);
                order.getOrderDetails().add(orderDetail);
                
                totalAmount = totalAmount.add(orderDetail.getSubtotal());
            }
        }
        
        order.setTotalAmount(totalAmount);
        orderRepository.save(order);
        
        return order;
    }
    
    public Order getOrderById(int id) {
        return orderRepository.findById(id).orElse(null);
    }
    
    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByIdDesc();
    }
    
    public void updateOrderStatus(int orderId, String status) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus(status);
            orderRepository.save(order);
        }
    }
}
