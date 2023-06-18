package com.carlosjr.products.products;

import com.carlosjr.products.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    public Product findProductById(Long productId, Long groupId){
        Product product = productRepository.findByProductIdAndGroupId(productId, groupId);
        if(product == null)
            throw new ResourceNotFoundException("The product with id " + productId + " was not found.");
        return product;
    }
    public List<Product> findAllProductsByGroup(Long groupId, PageRequest createdBy){
        return productRepository.findAllByGroup(groupId, createdBy);
    }
    public long createProduct(Product product){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String now = formatter.format(LocalDateTime.now());
        product.setCreationDate(now);
        product.setIsAvailable(true);
        Product savedProduct = productRepository.save(product);
        return savedProduct.getId();
    }
    /*public void deleteProduct(Long id) {
        productRepository.delete(findProductById(id));
    }*/
    @Scope("test")
    public void mockProducts(List<Product> products){
        productRepository.saveAll(products);
    }

}
