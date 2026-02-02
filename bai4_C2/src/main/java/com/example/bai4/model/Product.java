package com.example.bai4.model;

import jakarta.validation.constraints.*;

public class Product {
    private int id;

    @NotBlank(message = "Mã sản phẩm không được để trống")
    private String code;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String name;

    @NotNull(message = "Giá không được để trống")
    @Min(value = 1, message = "Giá phải từ 1 đồng trở lên")
    @Max(value = 9999999, message = "Giá không được vượt quá 9,999,999 đồng")
    private Long price;

    @Size(max = 200, message = "Tên file ảnh không được vượt quá 200 ký tự")
    private String image;

    @NotBlank(message = "Danh mục không được để trống")
    private String category;

    public Product() {
    }

    public Product(int id, String code, String name, Long price, String image, String category) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.price = price;
        this.image = image;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", image='" + image + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
