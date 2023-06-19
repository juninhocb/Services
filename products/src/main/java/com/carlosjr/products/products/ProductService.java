package com.carlosjr.products.products;

import com.carlosjr.products.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    private boolean isGroupAllowedToThisResource(Long productId, Long groupId){
        Product product = productRepository.findProductByProductIdAndGroupId(productId, groupId);
        return product != null;
    }
    public Product findAvailableProductByIdAndGroup(Long productId, Long groupId){
        Product product = productRepository.findAvailableByProductIdAndGroupId(productId, groupId);
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
        product.setDeletedDate(null);
        Product savedProduct = productRepository.save(product);
        return savedProduct.getId();
    }
    public void safeDeleteProduct(Long productId, Long groupId) {
        findAvailableProductByIdAndGroup(productId, groupId);
        LocalDate deletedDate = LocalDate.now();
        productRepository.changeAvailableState(productId, deletedDate, false);
    }

    /**
     * To avoid disclosing the existence of a resource,
     * returning a generic error message such as 'resource not found' can help prevent attackers from gaining knowledge about its presence.
     * @param productId
     * @param groupId
     */
    public void recoverResource(Long productId, Long groupId){
        if (isGroupAllowedToThisResource(productId, groupId))
            productRepository.changeAvailableState(productId, null, true);
        else
            throw new ResourceNotFoundException("The product with id " + productId + " was not found.");
    }
    public List<Product> findAllNotAvailable() {
        LocalDate comparableDay = LocalDate.now().plusDays(30);
        return productRepository.listOfNotAvailableProducts(comparableDay);
    }
    public void hardDelete(Product product) {
        productRepository.delete(product);
    }
    @Scope("test")
    public void mockProducts(List<Product> products){
        productRepository.saveAll(products);
    }
}
