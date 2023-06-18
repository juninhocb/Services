package com.carlosjr.products.config;

import com.carlosjr.products.products.Product;
import com.carlosjr.products.products.ProductService;
import com.carlosjr.products.products.enums.FeedSubProduct;
import com.carlosjr.products.products.enums.ProductType;
import com.carlosjr.products.products.enums.UnitType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Configuration
@Profile("test")
public class MockInitializer implements CommandLineRunner {
    @Autowired
    ProductService productService;

    @Override
    public void run(String... args) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String now = formatter.format(LocalDateTime.now());
        Product product1 = new Product(1L,1L, "Hamburguer", 1.25, ProductType.ALIMENTOS, FeedSubProduct.CAFE_PETISCOS, UnitType.UNIDADE, "Komprão", now);
        Product product2 = new Product(2L, 2L, "Maça", 5.25, ProductType.ALIMENTOS, FeedSubProduct.FRUTAS_LEGUMES, UnitType.KG, "Komprão", now);
        Product product3 = new Product(3L, 1L,  "Leite", 4.25, ProductType.ALIMENTOS, FeedSubProduct.CAFE_PETISCOS, UnitType.LITRO, "Komprão", now);
        productService.mockProducts(Arrays.asList(product1, product2, product3));
    }
}
