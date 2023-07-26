package com.carlosjr.products.sub.producttype;

import com.carlosjr.products.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product-type")
@RequiredArgsConstructor
public class ProductTypeResource {

    private final ProductTypeRepository productTypeRepository;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProductType(@RequestParam(name = "name") String typeName){
        productTypeRepository.save(ProductType.builder().name(typeName).build());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductType> retrieveAllProductTypes(Pageable pageable){
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "id")));

        return productTypeRepository.findAll(pageRequest).getContent();
    }

    @GetMapping("/find")
    @ResponseStatus(HttpStatus.OK)
    public ProductType findProductTypeByName(@RequestParam(name = "name") String name){
        return findProductType(name);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductType(String name){
        ProductType productType =  findProductType(name);
        productTypeRepository.delete(productType);
    }

    private ProductType findProductType(String name){
        Optional<ProductType> productType = productTypeRepository.findProductTypeByName(name);
        if (productType.isEmpty()){
            throw new ResourceNotFoundException("The resource with name " + name + " was not found");
        }
        return productType.get();
    }





}
