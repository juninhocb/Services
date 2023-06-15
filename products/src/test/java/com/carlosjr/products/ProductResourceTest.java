package com.carlosjr.products;

import com.carlosjr.products.products.Product;
import com.carlosjr.products.products.enums.FeedSubProduct;
import com.carlosjr.products.products.enums.ProductType;
import com.carlosjr.products.products.enums.UnitType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductResourceTest {
    @Autowired
    TestRestTemplate restTemplate;

    private Product product;

    @BeforeEach
    void setUp(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String now = formatter.format(LocalDateTime.now());
        product = new Product(4L,"Manteiga", 6.25, ProductType.ALIMENTOS, FeedSubProduct.CAFE_PETISCOS, UnitType.UNIDADE, "Komprão", now);
    }
    @Test
    public void shouldRetrieveAProductUsingValidId(){
        ResponseEntity<Product> response = restTemplate
                .getForEntity("/products/find/1", Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Product retrievedProduct = response.getBody();
        assertThat(retrievedProduct).isInstanceOf(Product.class);
        assertThat(retrievedProduct.getName()).isEqualTo("Hamburguer");
    }
    @Test
    public void shouldRespondNotFoundWhenTheResourceIsNotFound(){
        ResponseEntity<Product> response = restTemplate
                .getForEntity("/products/find/99", Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


}
