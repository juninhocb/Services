package com.carlosjr.products;

import com.carlosjr.products.products.Product;
import com.carlosjr.products.products.ProductDTO;
import com.carlosjr.products.products.enums.FeedSubProduct;
import com.carlosjr.products.products.enums.ProductType;
import com.carlosjr.products.products.enums.UnitType;
import org.apache.coyote.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductResourceTest {
    @Autowired
    TestRestTemplate restTemplate;
    private ProductDTO product;
    @BeforeEach
    void setUp(){
        product = new ProductDTO("Manteiga", 6.25, ProductType.ALIMENTOS, FeedSubProduct.CAFE_PETISCOS, UnitType.UNIDADE, "Komprão");
    }
    @Test
    public void shouldBeAbleToAccessPublicResource(){
        ResponseEntity<String> response = restTemplate
                .getForEntity("/products/public", String.class);
        assertThat(response.getBody()).isEqualTo("Successfully accessed");
    }
    @Test
    public void shouldRetrieveAProductUsingValidId(){
        ResponseEntity<Product> response = restTemplate
                .withBasicAuth("client", "client")
                .getForEntity("/products/find/1", Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Product retrievedProduct = response.getBody();
        assertThat(retrievedProduct).isInstanceOf(Product.class);
        assertThat(retrievedProduct.getName()).isEqualTo("Hamburguer");
    }
    @Test
    public void shouldRespondNotFoundWhenTheResourceWasNotFound(){
        ResponseEntity<Product> response = restTemplate
                .withBasicAuth("client", "client")
                .getForEntity("/products/find/99", Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test
    public void shouldCreateAndGetTheResourcePath(){
        ResponseEntity<Void> response = restTemplate
                .withBasicAuth("client", "client")
                .postForEntity("/products/create", product, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        URI resourcePath = response.getHeaders().getLocation();
        ResponseEntity<Product> response2 = restTemplate
                .withBasicAuth("client", "client")
                .getForEntity(resourcePath, Product.class);
        Product comparableProduct = response2.getBody();
        ProductDTO comparableProductDTO = new ProductDTO(
                comparableProduct.getName(),
                comparableProduct.getValue(),
                comparableProduct.getProductType(),
                comparableProduct.getFeedSubProduct(),
                comparableProduct.getUnitType(),
                comparableProduct.getMarketPlaceName()
        );
        assertThat(comparableProductDTO).isEqualTo(product);
    }
    @Test
    public void shouldNotCreateAResourceWhenTheProductIsInvalid(){
        Product invalidProduct = new Product(); //simulate DTO
        invalidProduct.setName("mação");
        invalidProduct.setValue(3.5);
        invalidProduct.setMarketPlaceName("Giassi");
        invalidProduct.setProductType(ProductType.COSMETICOS);
        ResponseEntity<Void> response = restTemplate
                .withBasicAuth("client", "client")
                .postForEntity("/products/create", invalidProduct, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        invalidProduct.setUnitType(UnitType.UNIDADE);
        invalidProduct.setValue(-2.6);
        ResponseEntity<Void> response2 = restTemplate
                .withBasicAuth("client", "client")
                .postForEntity("/products/create", invalidProduct, Void.class);
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        invalidProduct.setValue(3.5);
        invalidProduct.setName("pão2");
        ResponseEntity<Void> response3 = restTemplate
                .withBasicAuth("client", "client")
                .postForEntity("/products/create", invalidProduct, Void.class);
        assertThat(response3.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

}
