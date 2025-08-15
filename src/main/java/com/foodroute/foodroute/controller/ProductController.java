package com.foodroute.foodroute.controller;

import com.foodroute.foodroute.dto.ProductDto;
import com.foodroute.foodroute.model.Category;
import com.foodroute.foodroute.model.Product;
import com.foodroute.foodroute.service.CategoryService;
import com.foodroute.foodroute.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public Page<Product> getProductsPageable(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size,
                                             @RequestParam(defaultValue = "0") Double min,
                                             @RequestParam(defaultValue = "10000") Double max,
                                             @RequestParam(defaultValue = "") String search,
                                             @RequestParam(defaultValue = "-1") Long categoryId,
                                             @RequestParam(defaultValue = "default") String sortingType) {
        return productService.getProducts(page, size, min,max, search, categoryId, sortingType);
    }

    @GetMapping("/all")
    public List<Product> getProducts() {
        return productService.getProducts();
    }

    @GetMapping("/max")
    public Double getProductsMaxPrice() {
        List<Product> products = productService.getProducts();
        double max = products.get(0).getPrice();
        for (Product p : products) {
            max = Math.max(max, p.getPrice());
        }
        return max;
    }

    @GetMapping("/{id}")
    public Optional<Product> getProducts(@PathVariable Long id) {
        System.out.println(productService.getProductById(id).get());
    return productService.getProductById(id);
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductDto productDto) {
        try {
            Optional<Category> category = categoryService.getCategoryById(productDto.getCategoryId());
            if(!category.isPresent()) throw new RuntimeException("Category not found");
            Product product = new Product(productDto.getName(), productDto.getDescription(), productDto.getPrice(), productDto.getImage(), category.get());
            productService.createProduct(product);
            return ResponseEntity.ok(product);
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id,@RequestBody ProductDto productDto) {
        try {
            Optional<Category> category = categoryService.getCategoryById(productDto.getCategoryId());
            if(!category.isPresent()) throw new RuntimeException("Category not found");
            Product product = new Product(productDto.getName(), productDto.getDescription(), productDto.getPrice(), productDto.getImage(), category.get());
            Product updatedProduct = productService.updateProduct(id, product);
            return ResponseEntity.ok(updatedProduct);
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
