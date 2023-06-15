package com.carlosjr.products.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
