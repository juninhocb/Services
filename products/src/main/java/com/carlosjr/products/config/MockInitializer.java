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

        Product product1 = Product.builder()
                .name("Hamburguer")
                .value(1.25)
                .productType(ProductType.ALIMENTOS)
                .FeedSubProduct(FeedSubProduct.CAFE_PETISCOS)
                .unitType(UnitType.UNIDADE)
                .marketPlaceName("Komprão")
                .isAvailable(true)
                .ownerGroup(1L).build();

        Product product2 = Product.builder()
                .name("Maça")
                .value(5.25)
                .productType(ProductType.ALIMENTOS)
                .FeedSubProduct(FeedSubProduct.FRUTAS_LEGUMES)
                .unitType(UnitType.KG)
                .marketPlaceName("Komprão")
                .isAvailable(true)
                .ownerGroup(1L).build();

        Product product3 = Product.builder()
                .name("Leite")
                .value(4.25)
                .productType(ProductType.ALIMENTOS)
                .FeedSubProduct(FeedSubProduct.CAFE_PETISCOS)
                .unitType(UnitType.LITRO)
                .marketPlaceName("Komprão")
                .isAvailable(true)
                .ownerGroup(1L).build();

        Product product4 = Product.builder()
                .name("Ketchup")
                .value(7.80)
                .productType(ProductType.ALIMENTOS)
                .FeedSubProduct(FeedSubProduct.CAFE_PETISCOS)
                .unitType(UnitType.UNIDADE)
                .marketPlaceName("Komprão")
                .isAvailable(true)
                .ownerGroup(1L).build();
        productService.mockProducts(Arrays.asList(product1, product2, product3, product4));
    }
}
