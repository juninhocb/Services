package com.carlosjr.products.config;

import com.carlosjr.products.products.Product;
import com.carlosjr.products.products.ProductService;
import com.carlosjr.products.products.enums.ProductType;
import com.carlosjr.products.products.enums.UnitType;
import com.carlosjr.products.subproduct.SubProduct;
import com.carlosjr.products.subproduct.SubProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Profile("test")
public class MockInitializer implements CommandLineRunner {

    private final ProductService productService;
    private final SubProductRepository subProductRepository;
    @Override
    public void run(String... args) {

        SubProduct subProduct1 = SubProduct.builder().name("CAFE_PETISCOS").build();
        SubProduct subProduct2 = SubProduct.builder().name("FRUTAS_LEGUMES").build();

        List<SubProduct> persistedSubProducts =  subProductRepository
                .saveAll(Arrays.asList(subProduct1, subProduct2));


        Product product1 = Product.builder()
                .name("Hamburguer")
                .value(1.25)
                .productType(ProductType.ALIMENTOS)
                .subProduct(persistedSubProducts.get(0))
                .unitType(UnitType.UNIDADE)
                .marketPlaceName("Komprão")
                .isAvailable(true)
                .ownerGroup(1L).build();

        Product product2 = Product.builder()
                .name("Maça")
                .value(5.25)
                .productType(ProductType.ALIMENTOS)
                .subProduct(persistedSubProducts.get(1))
                .unitType(UnitType.KG)
                .marketPlaceName("Komprão")
                .isAvailable(true)
                .ownerGroup(1L).build();

        Product product3 = Product.builder()
                .name("Leite")
                .value(4.25)
                .productType(ProductType.ALIMENTOS)
                .subProduct(persistedSubProducts.get(0))
                .unitType(UnitType.LITRO)
                .marketPlaceName("Komprão")
                .isAvailable(true)
                .ownerGroup(1L).build();

        Product product4 = Product.builder()
                .name("Ketchup")
                .value(7.80)
                .productType(ProductType.ALIMENTOS)
                .subProduct(persistedSubProducts.get(0))
                .unitType(UnitType.UNIDADE)
                .marketPlaceName("Komprão")
                .isAvailable(true)
                .ownerGroup(1L).build();
        productService.mockProducts(Arrays.asList(product1, product2, product3, product4));
    }
}
