package com.carlosjr.products;

import com.carlosjr.products.products.Product;
import com.carlosjr.products.products.ProductDTO;
import com.carlosjr.products.products.ProductService;
import com.carlosjr.products.sub.producttype.ProductType;
import com.carlosjr.products.sub.producttype.ProductTypeRepository;
import com.carlosjr.products.sub.subproduct.SubProduct;
import com.carlosjr.products.sub.subproduct.SubProductRepository;
import com.carlosjr.products.sub.unittype.UnitType;
import com.carlosjr.products.sub.unittype.UnitTypeRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class

ProductResourceTest {
    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    SubProductRepository subProductRepository;
    @Autowired
    UnitTypeRepository unitTypeRepository;
    @Autowired
    ProductTypeRepository productTypeRepository;
    @Autowired
    ProductService productService;
    private ProductDTO product;
    private String basicUser;
    private String basicPassword;
    private ProductType productType;
    private SubProduct subProduct;
    private UnitType unitType;

    @BeforeEach
    void setUp(){
        productType = productTypeRepository.save(ProductType.builder().name("BESTEIRAS").build());
        subProduct = subProductRepository.save(SubProduct.builder().name("TEMPEIROS").build());
        unitType = unitTypeRepository.save(UnitType.builder().name("PACOTE").build());
        basicUser = "full-client";
        basicPassword = "admin";
        product = new ProductDTO(1L, "Manteiga", 6.25, productType, subProduct, unitType, "Komprão");
    }
    @Test
    public void shouldBeAbleToAccessPublicResource(){
        ResponseEntity<String> response = restTemplate
                .getForEntity("/v1/products/available", String.class);
        assertThat(response.getBody()).isEqualTo("API is running");
    }
    @Test
    public void shouldRetrieveAProductUsingValidId(){
        Product firstProductOfGroup = productService.
                findAllProductsByGroup(1L, PageRequest.of(0,1)).get(0);
        ResponseEntity<Product> response = restTemplate
                .withBasicAuth(basicUser, basicPassword)
                .getForEntity(String.format("/v1/products/find/%s/1", firstProductOfGroup.getId()) , Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Product retrievedProduct = response.getBody();
        assertThat(retrievedProduct).isInstanceOf(Product.class);
        assertThat(retrievedProduct.getName()).isNotNull();
    }
    @Test
    public void shouldRespondNotFoundWhenTheResourceWasNotFound(){
        ResponseEntity<Product> response = restTemplate
                .withBasicAuth(basicUser, basicPassword)
                .getForEntity(String.format("/v1/products/find/%s/1", UUID.randomUUID()) , Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test
    @DirtiesContext
    public void shouldCreateAndGetTheResourcePath(){
        ResponseEntity<Void> response = restTemplate
                .withBasicAuth(basicUser, basicPassword)
                .postForEntity("/v1/products/create", product, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        URI resourcePath = response.getHeaders().getLocation();
        ResponseEntity<Product> response2 = restTemplate
                .withBasicAuth(basicUser, basicPassword)
                .getForEntity(resourcePath, Product.class);
        Product comparableProduct = response2.getBody();
        assert comparableProduct != null;
        ProductDTO comparableProductDTO = new ProductDTO(
                comparableProduct.getOwnerGroup(),
                comparableProduct.getName(),
                comparableProduct.getValue(),
                comparableProduct.getProductType(),
                comparableProduct.getSubProduct(),
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
        invalidProduct.setProductType(productType);
        ResponseEntity<Void> response = restTemplate
                .withBasicAuth(basicUser, basicPassword)
                .postForEntity("/v1/products/create", invalidProduct, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        invalidProduct.setUnitType(unitType);
        invalidProduct.setValue(-2.6);
        ResponseEntity<Void> response2 = restTemplate
                .withBasicAuth(basicUser, basicPassword)
                .postForEntity("/v1/products/create", invalidProduct, Void.class);
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        invalidProduct.setValue(3.5);
        invalidProduct.setName("pão2");
        ResponseEntity<Void> response3 = restTemplate
                .withBasicAuth(basicUser, basicPassword)
                .postForEntity("/v1/products/create", invalidProduct, Void.class);
        assertThat(response3.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    public void shouldRetrieveAllItemsThatBelongsAGroup(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(Base64.encodeBase64String(String.format("%s:%s", basicUser,basicPassword).getBytes(StandardCharsets.UTF_8)));
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<List<Product>> response = restTemplate
                .exchange(
                        "/v1/products/find-all/group/1?page=0&size=2",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<List<Product>>() {}
                );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(2);
    }
    @Test
    public void shouldNotRetrieveAnItemWhenTheOwnerIsNotAllowed(){
        Product firstProductOfGroup = productService.
                findAllProductsByGroup(1L, PageRequest.of(0,1)).get(0);
        ResponseEntity<Product> response = restTemplate
                .withBasicAuth(basicUser, basicPassword)
                .getForEntity(String.format("/v1/products/find/%s/3", firstProductOfGroup.getId()) , Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test
    @DirtiesContext
    public void shouldTemporarilyDeleteAResource(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(Base64.encodeBase64String(String.format("%s:%s", basicUser, basicPassword).getBytes(StandardCharsets.UTF_8)));
        Product firstProductOfGroup = productService.
                findAllProductsByGroup(1L, PageRequest.of(0,1)).get(0);
        ResponseEntity<Void> response = restTemplate
                .exchange(
                        String.format("/v1/products/safe-delete/%s/1", firstProductOfGroup.getId()) ,
                        HttpMethod.PUT,
                        new HttpEntity<>(headers),
                        Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        ResponseEntity<Product> response2 = restTemplate
                .withBasicAuth(basicUser, basicPassword)
                .getForEntity(String.format("/v1/products/find/%s/1", firstProductOfGroup.getId()) , Product.class);
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test
    @DirtiesContext
    public void shouldRecoverAResourceAfterDelete(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(Base64.encodeBase64String(String.format("%s:%s",basicUser, basicPassword).getBytes(StandardCharsets.UTF_8)));
        Product firstProductOfGroup = productService.
                findAllProductsByGroup(1L, PageRequest.of(0,1)).get(0);
        ResponseEntity<Void> response = restTemplate
                .exchange(
                        String.format("/v1/products/safe-delete/%s/1", firstProductOfGroup.getId()),
                        HttpMethod.PUT,
                        new HttpEntity<>(headers),
                        Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        ResponseEntity<Void> response2 = restTemplate
                .exchange(
                        String.format("/v1/products/recover/%s/1", firstProductOfGroup.getId()),
                        HttpMethod.PUT,
                        new HttpEntity<>(headers),
                        Void.class
                        );
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
        ResponseEntity<Product> response3 = restTemplate
                .withBasicAuth(basicUser, basicPassword)
                .getForEntity(  String.format("/v1/products/find/%s/1", firstProductOfGroup.getId()) , Product.class);
        assertThat(response3.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    @DirtiesContext
    public void shouldForbidUnauthorizedClientToCreateAResource(){
        ResponseEntity<Void> response = restTemplate
                .withBasicAuth("safe-client", "basic")
                .postForEntity("/v1/products/create", product, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

}
