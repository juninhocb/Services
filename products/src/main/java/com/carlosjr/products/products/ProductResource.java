package com.carlosjr.products.products;

import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/products")
public class ProductResource {
    @Autowired
    ProductService productService;
    @GetMapping(value = "/find/{productId}")
    public ResponseEntity<Product> findProductById(@PathVariable Long productId) {
        Product product = productService.findProductById(productId);
        return ResponseEntity.ok().body(product);
    }
    @PostMapping(value = "/create")
    public ResponseEntity<Void> createProduct(@RequestBody @Valid ProductDTO productDTO, UriComponentsBuilder ucb){
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        long productId = productService.createProduct(product);
        URI resourcePath = ucb
                .path("/products/find/{productId}")
                .buildAndExpand(productId)
                .toUri();
        return ResponseEntity.created(resourcePath).build();
    }

    @GetMapping(value = "/public")
    public ResponseEntity<String> publicRequest(){
        return ResponseEntity.ok().body("Successfully accessed");
    }



}
