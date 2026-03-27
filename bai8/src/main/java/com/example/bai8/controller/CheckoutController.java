package com.example.bai8.controller;

import com.example.bai8.model.Order;
import com.example.bai8.service.CartService;
import com.example.bai8.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private OrderService orderService;
    
    // Câu 7: Hiển thị trang checkout
    @GetMapping
    public String showCheckout(Model model) {
        if (cartService.getCartItems().isEmpty()) {
            return "redirect:/cart";
        }
        
        model.addAttribute("cartItems", cartService.getCartItems());
        model.addAttribute("totalPrice", cartService.getTotalPrice());
        return "checkout";
    }
    
    // Câu 7: Xử lý đặt hàng
    @PostMapping("/create-order")
    public String createOrder() {
        if (cartService.getCartItems().isEmpty()) {
            return "redirect:/cart";
        }
        
        Order order = orderService.createOrderFromCart(cartService.getCartItems());
        
        if (order != null) {
            cartService.clearCart();
            return "redirect:/checkout/success/" + order.getId();
        }
        
        return "redirect:/checkout";
    }
    
    // Hiển thị trang thành công
    @GetMapping("/success/{orderId}")
    public String showSuccess(@PathVariable int orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            return "redirect:/products";
        }
        
        model.addAttribute("order", order);
        return "checkout-success";
    }
}
