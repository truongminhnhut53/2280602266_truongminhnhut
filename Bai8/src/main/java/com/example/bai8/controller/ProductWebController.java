package com.example.bai8.controller;

import com.example.bai8.model.Product;
import com.example.bai8.service.CartService;
import com.example.bai8.service.CategoryService;
import com.example.bai8.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductWebController {

    private static final Path UPLOAD_BASE_DIR = Paths.get(System.getProperty("user.dir"), "uploads");
    private static final int PAGE_SIZE = 5;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CartService cartService;

    @GetMapping
    public String getAllProducts(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        Page<Product> productPage = productService.getProductsWithFilters(keyword, category, sort, page, PAGE_SIZE);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("currentPage", productPage.getNumber());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);
        model.addAttribute("sort", sort);
        model.addAttribute("cartSize", cartService.getCartSize());

        return "products";
    }

    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "add-product";
    }

    @PostMapping("/add")
    public String addProduct(
            @Valid @ModelAttribute Product product,
            BindingResult result,
            @RequestParam(value = "file", required = false) MultipartFile file,
            Model model) {

        model.addAttribute("categories", categoryService.getAllCategories());
        if (result.hasErrors()) {
            return "add-product";
        }

        if (file != null && !file.isEmpty()) {
            try {
                product.setImage(saveUploadAndGetFileName(file));
            } catch (IOException e) {
                model.addAttribute("error", "Loi khi upload file: " + e.getMessage());
                return "add-product";
            }
        } else {
            product.setImage("default.jpg");
        }

        try {
            productService.addProduct(product);
        } catch (IllegalStateException e) {
            if (ProductService.ERR_DUPLICATE_CODE.equals(e.getMessage())) {
                model.addAttribute("error", "Ma san pham da ton tai. Vui long dung ma khac.");
            } else {
                model.addAttribute("error", "Khong the them san pham. Vui long kiem tra lai du lieu.");
            }
            return "add-product";
        } catch (RuntimeException e) {
            model.addAttribute("error", "Khong the them san pham. Vui long kiem tra lai du lieu.");
            return "add-product";
        }
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable int id, Model model) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return "redirect:/products";
        }

        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "edit-product";
    }

    @PostMapping("/edit/{id}")
    public String updateProduct(
            @PathVariable int id,
            @Valid @ModelAttribute Product product,
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
                product.setImage(saveUploadAndGetFileName(file));
            } catch (IOException e) {
                model.addAttribute("error", "Loi khi upload file: " + e.getMessage());
                return "edit-product";
            }
        } else {
            product.setImage(existingProduct.getImage());
        }

        product.setId(id);
        try {
            productService.updateProduct(product);
        } catch (IllegalStateException e) {
            if (ProductService.ERR_DUPLICATE_CODE.equals(e.getMessage())) {
                model.addAttribute("error", "Ma san pham da ton tai. Vui long dung ma khac.");
            } else {
                model.addAttribute("error", "Khong the cap nhat san pham. Vui long kiem tra lai du lieu.");
            }
            return "edit-product";
        } catch (RuntimeException e) {
            model.addAttribute("error", "Khong the cap nhat san pham. Vui long kiem tra lai du lieu.");
            return "edit-product";
        }
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("successMessage", "Da xoa san pham thanh cong.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "San pham khong ton tai hoac da bi xoa.");
        } catch (IllegalStateException e) {
            if (ProductService.ERR_PRODUCT_IN_ORDER.equals(e.getMessage())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Khong the xoa: san pham da duoc su dung trong don hang.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Khong the xoa san pham do loi he thong.");
            }
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Khong the xoa: san pham da duoc su dung trong don hang.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Khong the xoa san pham do loi he thong.");
        }
        return "redirect:/products";
    }

    private String saveUploadAndGetFileName(MultipartFile file) throws IOException {
        Files.createDirectories(UPLOAD_BASE_DIR);
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            return "default.jpg";
        }

        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = UUID.randomUUID() + extension;
        Path destination = UPLOAD_BASE_DIR.resolve(newFilename);
        Files.write(destination, file.getBytes());
        return newFilename;
    }
}
