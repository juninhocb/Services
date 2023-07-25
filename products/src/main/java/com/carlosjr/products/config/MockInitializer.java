package com.carlosjr.products.config;

import com.carlosjr.products.products.Product;
import com.carlosjr.products.products.ProductService;
import com.carlosjr.products.producttype.ProductType;
import com.carlosjr.products.producttype.ProductTypeRepository;
import com.carlosjr.products.subproduct.SubProduct;
import com.carlosjr.products.subproduct.SubProductRepository;
import com.carlosjr.products.unittype.UnitType;
import com.carlosjr.products.unittype.UnitTypeRepository;
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
    private final ProductTypeRepository productTypeRepository;
    private final UnitTypeRepository unitTypeRepository;
    @Override
    public void run(String... args) {

        SubProduct subProduct1 = SubProduct.builder().name("CAFE_PETISCOS").build();
        SubProduct subProduct2 = SubProduct.builder().name("FRUTAS_LEGUMES").build();
        ProductType productType = ProductType.builder().name("Alimentos").build();
        UnitType unitType1 = UnitType.builder().name("UNIDADE").build();
        UnitType unitType2 = UnitType.builder().name("KILO").build();
        UnitType unitType3 = UnitType.builder().name("LITRO").build();


        List<SubProduct> persistedSubProducts =  subProductRepository
                .saveAll(Arrays.asList(subProduct1, subProduct2));

        ProductType persistedProductType = productTypeRepository
                .save(productType);

        List<UnitType> persistedUnitTypes = unitTypeRepository.
                saveAll(Arrays.asList(unitType1, unitType2, unitType3));


        Product product1 = Product.builder()
                .name("Hamburguer")
                .value(1.25)
                .productType(persistedProductType)
                .subProduct(persistedSubProducts.get(0))
                .unitType(persistedUnitTypes.get(0))
                .marketPlaceName("Komprão")
                .isAvailable(true)
                .ownerGroup(1L).build();

        Product product2 = Product.builder()
                .name("Maça")
                .value(5.25)
                .productType(persistedProductType)
                .subProduct(persistedSubProducts.get(1))
                .unitType(persistedUnitTypes.get(1))
                .marketPlaceName("Komprão")
                .isAvailable(true)
                .ownerGroup(1L).build();

        Product product3 = Product.builder()
                .name("Leite")
                .value(4.25)
                .productType(persistedProductType)
                .subProduct(persistedSubProducts.get(0))
                .unitType(persistedUnitTypes.get(2))
                .marketPlaceName("Komprão")
                .isAvailable(true)
                .ownerGroup(1L).build();

        Product product4 = Product.builder()
                .name("Ketchup")
                .value(7.80)
                .productType(persistedProductType)
                .subProduct(persistedSubProducts.get(0))
                .unitType(persistedUnitTypes.get(0))
                .marketPlaceName("Komprão")
                .isAvailable(true)
                .ownerGroup(1L).build();
        productService.mockProducts(Arrays.asList(product1, product2, product3, product4));
    }
}
