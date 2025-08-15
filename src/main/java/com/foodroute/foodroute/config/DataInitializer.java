package com.foodroute.foodroute.config;

import com.foodroute.foodroute.model.Category;
import com.foodroute.foodroute.model.Product;
import com.foodroute.foodroute.repository.CategoryRepository;
import com.foodroute.foodroute.repository.ProductRepository;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final Faker faker;

    public DataInitializer(ProductRepository productRepository, CategoryRepository categoryRepository, Faker faker) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.faker = faker;
    }

    @Override
    public void run(String... args) throws Exception {

//        List<Category> categories = categoryRepository.findAll();
//        for (int i = 0; i < 50; i++) {
//            Product product = new Product();
//            product.setName(faker.commerce().productName());
//            product.setPrice(Double.parseDouble(faker.commerce().price(10, 1000)));
//            product.setImage("https://picsum.photos/640/480?random=" + i);
//            product.setCategory(categories.get(
//                    (int)Math.floor(Math.random() * categories.size())
//            ));
//            product.setDescription(faker.lorem().paragraph());
//            productRepository.save(product);
//        }
//
//        System.out.println("Generated 50 fake customers");
    }
}