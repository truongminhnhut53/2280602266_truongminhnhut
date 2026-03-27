package com.example.bai8.model;

import java.io.Serializable;

public class CartItem implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int productId;
    private String name;
    private Long price;
    private String image;
    private int quantity;
    
    public CartItem() {
    }
    
    public CartItem(int productId, String name, Long price, String image) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.image = image;
        this.quantity = 1;
    }
    
    public int getProductId() {
        return productId;
    }
    
    public void setProductId(int productId) {
        this.productId = productId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Long getPrice() {
        return price;
    }
    
    public void setPrice(Long price) {
        this.price = price;
    }
    
    public String getImage() {
        return image;
    }
    
    public void setImage(String image) {
        this.image = image;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public Long getTotal() {
        return price * quantity;
    }
}
