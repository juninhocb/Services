package com.carlosjr.products.products;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, PagingAndSortingRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.ownerGroup = :groupId AND p.isAvailable = true")
    List<Product> findAllByGroup(@Param("groupId") Long groupId, PageRequest createdDate);
    @Query("SELECT p FROM Product p WHERE p.id = :productId AND p.ownerGroup = :groupId AND p.isAvailable = true")
    Product findByProductIdAndGroupId(@Param("productId") Long productId, @Param("groupId") Long groupId);

}
