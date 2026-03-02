package com.example.bai4.controller;

import com.example.bai4.model.Product;
import com.example.bai4.service.CategoryService;
import com.example.bai4.service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    @GetMapping
    public String listProducts(Model model, HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        model.addAttribute("products", productService.getAllProducts());
        return "product/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product/add";
    }

    @PostMapping("/add")
    public String saveProduct(@Valid @ModelAttribute("product") Product product,
                              BindingResult result,
                              @RequestParam(value = "file", required = false) MultipartFile file,
                              Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());

        if (result.hasErrors()) {
            return "product/add";
        }

        if (file != null && !file.isEmpty()) {
            try {
                String fileName = saveImage(file);
                product.setImage(fileName);
            } catch (IOException e) {
                model.addAttribute("error", "Lỗi khi upload file: " + e.getMessage());
                return "product/add";
            }
        } else {
            product.setImage("default.jpg");
        }

        productService.addProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return "redirect:/products";
        }
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "edit-product";
    }

    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable Integer id,
                                @Valid @ModelAttribute("product") Product product,
                                BindingResult result,
                                @RequestParam(value = "file", required = false) MultipartFile file,
                                Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());

        if (result.hasErrors()) {
            return "edit-product";
        }

        Product existingProduct = productService.getProductById(id);
        if (existingProduct == null) {
            return "redirect:/products";
        }

        if (file != null && !file.isEmpty()) {
            try {
                String fileName = saveImage(file);
                product.setImage(fileName);
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
    public String deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }

    private String saveImage(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        } else {
            fileExtension = ".jpg";
        }

        String newFilename = UUID.randomUUID() + fileExtension;
        Path filePath = uploadPath.resolve(newFilename);
        Files.write(filePath, file.getBytes());
        return newFilename;
    }
}

