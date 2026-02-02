package com.example.bai4.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.bai4.model.Product;
import com.example.bai4.service.ProductService;
import com.example.bai4.service.CategoryService;

import jakarta.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductWebController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    @GetMapping
    public String getAllProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products";
    }

    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "add-product";
    }

    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute Product product,
                            BindingResult result,
                            @RequestParam(value = "file", required = false) MultipartFile file,
                            Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());

        if (result.hasErrors()) {
            return "add-product";
        }

        if (file != null && !file.isEmpty()) {
            try {
                Files.createDirectories(Paths.get(UPLOAD_DIR));
                String originalFilename = file.getOriginalFilename();
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String newFilename = UUID.randomUUID() + fileExtension;
                Path filePath = Paths.get(UPLOAD_DIR, newFilename);
                Files.write(filePath, file.getBytes());
                product.setImage(newFilename);
            } catch (IOException e) {
                model.addAttribute("error", "Lỗi khi upload file: " + e.getMessage());
                return "add-product";
            }
        } else {
            product.setImage("default.jpg");
        }

        int maxId = productService.getAllProducts().stream()
                .mapToInt(Product::getId)
                .max()
                .orElse(0);
        product.setId(maxId + 1);

        productService.addProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable int id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "edit-product";
    }

    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable int id,
                               @Valid @ModelAttribute Product product,
                               BindingResult result,
                               @RequestParam(value = "file", required = false) MultipartFile file,
                               Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());

        if (result.hasErrors()) {
            return "edit-product";
        }

        Product existingProduct = productService.getProductById(id);

        if (file != null && !file.isEmpty()) {
            try {
                Files.createDirectories(Paths.get(UPLOAD_DIR));
                String originalFilename = file.getOriginalFilename();
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String newFilename = UUID.randomUUID() + fileExtension;
                Path filePath = Paths.get(UPLOAD_DIR, newFilename);
                Files.write(filePath, file.getBytes());
                product.setImage(newFilename);
            } catch (IOException e) {
                model.addAttribute("error", "Lỗi khi upload file: " + e.getMessage());
                return "edit-product";
            }
        } else {
            product.setImage(existingProduct.getImage());
        }

        product.setId(id);
        productService.updateProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }
}
