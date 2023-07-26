package com.carlosjr.products.products;

import com.carlosjr.products.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private boolean isGroupAllowedToThisResource(UUID productId, Long groupId){
        Product product = productRepository.findProductByProductIdAndGroupId(productId, groupId);
        return product != null;
    }
    public Product findAvailableProductByIdAndGroup(UUID productId, Long groupId){
        Product product = productRepository.findAvailableByProductIdAndGroupId(productId, groupId);
        if(product == null)
            throw new ResourceNotFoundException("The product with id " + productId + " was not found.");
        return product;
    }
    @Cacheable(cacheNames = "product-cache", key = "#groupId")
    public List<Product> findAllProductsByGroup(Long groupId, PageRequest pageRequest){
        log.trace("[ ProductService ] Attempt to find products by group");
        return productRepository.findAllByGroup(groupId, pageRequest);
    }

    public UUID createProduct(Product product){
        product.setIsAvailable(true);
        product.setDeletedDate(null);
        Product savedProduct = productRepository.save(product);
        return savedProduct.getId();
    }
    public void safeDeleteProduct(UUID productId, Long groupId) {
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
    public void recoverResource(UUID productId, Long groupId){
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
