package com.carlosjr.products;

import com.carlosjr.products.products.Product;
import com.carlosjr.products.products.ProductDTO;
import com.carlosjr.products.products.enums.FeedSubProduct;
import com.carlosjr.products.products.enums.ProductType;
import com.carlosjr.products.products.enums.UnitType;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductResourceTest {
    @Autowired
    TestRestTemplate restTemplate;
    private ProductDTO product;
    private String basicUser;
    private String basicPassword;
    @BeforeEach
    void setUp(){
        basicUser = "client";
        basicPassword = "client";
        product = new ProductDTO(1L, "Manteiga", 6.25, ProductType.ALIMENTOS, FeedSubProduct.CAFE_PETISCOS, UnitType.UNIDADE, "Komprão");
    }
    @Test
    public void shouldBeAbleToAccessPublicResource(){
        ResponseEntity<String> response = restTemplate
                .getForEntity("/products/available", String.class);
        assertThat(response.getBody()).isEqualTo("API is running");
    }
    @Test
    public void shouldRetrieveAProductUsingValidId(){
        ResponseEntity<Product> response = restTemplate
                .withBasicAuth(basicUser, basicPassword)
                .getForEntity("/products/find/1", Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Product retrievedProduct = response.getBody();
        assertThat(retrievedProduct).isInstanceOf(Product.class);
        assertThat(retrievedProduct.getName()).isEqualTo("Hamburguer");
    }
    @Test
    public void shouldRespondNotFoundWhenTheResourceWasNotFound(){
        ResponseEntity<Product> response = restTemplate
                .withBasicAuth(basicUser, basicPassword)
                .getForEntity("/products/find/99", Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test
    @DirtiesContext
    public void shouldCreateAndGetTheResourcePath(){
        ResponseEntity<Void> response = restTemplate
                .withBasicAuth(basicUser, basicPassword)
                .postForEntity("/products/create", product, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        URI resourcePath = response.getHeaders().getLocation();
        ResponseEntity<Product> response2 = restTemplate
                .withBasicAuth(basicUser, basicPassword)
                .getForEntity(resourcePath, Product.class);
        Product comparableProduct = response2.getBody();
        ProductDTO comparableProductDTO = new ProductDTO(
                comparableProduct.getOwnerGroup(),
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
                .withBasicAuth(basicUser, basicPassword)
                .postForEntity("/products/create", invalidProduct, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        invalidProduct.setUnitType(UnitType.UNIDADE);
        invalidProduct.setValue(-2.6);
        ResponseEntity<Void> response2 = restTemplate
                .withBasicAuth(basicUser, basicPassword)
                .postForEntity("/products/create", invalidProduct, Void.class);
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        invalidProduct.setValue(3.5);
        invalidProduct.setName("pão2");
        ResponseEntity<Void> response3 = restTemplate
                .withBasicAuth(basicUser, basicPassword)
                .postForEntity("/products/create", invalidProduct, Void.class);
        assertThat(response3.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    public void shouldRetrieveAllItemsThatBelongsAGroup(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(Base64.encodeBase64String(String.format("%s:%s", basicUser,basicPassword).getBytes(StandardCharsets.UTF_8)));
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<List<Product>> response = restTemplate
                .exchange(
                        "/products/find-all/group/1?page=0&size=2",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<List<Product>>() {}
                );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(2);
    }


}
