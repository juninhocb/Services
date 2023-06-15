package com.carlosjr.products.products;

import com.carlosjr.products.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    public Product findProductById(Long id){
        Optional<Product> productOpt = productRepository.findById(id);
        if(productOpt.isEmpty())
            throw new ResourceNotFoundException("The product with id " + id + " was not found.");
        return productOpt.get();
    }
    public List<Product> findAllProducts(){
        return productRepository.findAll();
    }
    public void createProduct(Product product){
        productRepository.save(product);
    }
    public void deleteProduct(Long id) throws Exception {
        productRepository.delete(findProductById(id));
    }
    @Scope("test")
    public void mockProducts(List<Product> products){
        productRepository.saveAll(products);
    }

}
