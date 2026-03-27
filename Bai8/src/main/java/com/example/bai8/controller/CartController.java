package com.example.bai8.controller;

import com.example.bai8.model.Product;
import com.example.bai8.service.CartService;
import com.example.bai8.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private ProductService productService;
    
    // Câu 5: Thêm sản phẩm vào giỏ hàng
    @PostMapping("/add/{id}")
    public String addToCart(@PathVariable int id, 
                           @RequestParam(value = "quantity", defaultValue = "1") int quantity) {
        Product product = productService.getProductById(id);
        if (product != null) {
            cartService.addToCartWithQuantity(
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    product.getImage(),
                    quantity
            );
        }
        return "redirect:/cart";
    }
    
    // Câu 6: Hiển thị trang giỏ hàng
    @GetMapping
    public String viewCart(Model model) {
        model.addAttribute("cartItems", cartService.getCartItems());
        model.addAttribute("totalPrice", cartService.getTotalPrice());
        return "cart";
    }
    
    // Cập nhật số lượng
    @PostMapping("/update/{id}")
    public String updateQuantity(@PathVariable int id,
                                @RequestParam int quantity) {
        cartService.updateQuantity(id, quantity);
        return "redirect:/cart";
    }
    
    // Xóa sản phẩm khỏi giỏ
    @GetMapping("/remove/{id}")
    public String removeFromCart(@PathVariable int id) {
        cartService.removeFromCart(id);
        return "redirect:/cart";
    }
    
    // Xóa toàn bộ giỏ hàng
    @GetMapping("/clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/cart";
    }
}
