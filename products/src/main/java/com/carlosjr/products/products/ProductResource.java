package com.carlosjr.products.products;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/v1/products")
@RequiredArgsConstructor
public class ProductResource {

    private final ProductService productService;
    @GetMapping(value = "/find/{productId}/{groupId}")
    public ResponseEntity<Product> findProductById(@PathVariable(name="productId") Long productId,@PathVariable(name="groupId") Long groupId) {
        Product product = productService.findAvailableProductByIdAndGroup(productId, groupId);
        return ResponseEntity.ok().body(product);
    }
    @PostMapping(value = "/create")
    public ResponseEntity<Void> createProduct(@RequestBody @Valid ProductDTO productDTO, UriComponentsBuilder ucb){
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        UUID productId = productService.createProduct(product);
        URI resourcePath = ucb
                .path("/v1/products/find/{productId}/{groupId}")
                .buildAndExpand(productId, productDTO.ownerGroup())
                .toUri();
        return ResponseEntity.created(resourcePath).build();
    }
    @GetMapping(value = "/find-all/group/{groupId}")
    public ResponseEntity<List<Product>> findAllByGroup(@PathVariable(name = "groupId") Long groupId, Pageable pageable){
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSortOr(Sort.by(Sort.Direction.ASC,"creationDate" )));
        List<Product> products = productService.findAllProductsByGroup(groupId, pageRequest);
        return ResponseEntity.ok().body(products);
    }
    @PutMapping(value = "/safe-delete/{productId}/{groupId}")
    public ResponseEntity<Void> safeDelete(@PathVariable(name = "productId") Long productId, @PathVariable(name = "groupId") Long groupId){
        productService.safeDeleteProduct(productId, groupId);
        return ResponseEntity.ok().build();
    }
    @PutMapping(value = "/recover/{productId}/{groupId}")
    public ResponseEntity<Void> recoverResource(@PathVariable(name = "productId") Long productId, @PathVariable(name = "groupId") Long groupId){
        productService.recoverResource(productId, groupId);
        return ResponseEntity.ok().build();
    }
    @GetMapping(value = "/available")
    public ResponseEntity<String> publicRequest(){
        return ResponseEntity.ok().body("API is running");
    }

}
