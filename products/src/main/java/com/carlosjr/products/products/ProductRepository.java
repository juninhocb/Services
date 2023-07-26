package com.carlosjr.products.products;


import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, PagingAndSortingRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.ownerGroup = :groupId AND p.isAvailable = true")
    List<Product> findAllByGroup(@Param("groupId") Long groupId, PageRequest createdDate);
    @Query("SELECT p FROM Product p WHERE p.id = :productId AND p.ownerGroup = :groupId AND p.isAvailable = true")
    Product findAvailableByProductIdAndGroupId(@Param("productId") UUID productId, @Param("groupId") Long groupId);
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.isAvailable = :isAvailable , p.deletedDate = :deletedDate WHERE p.id = :productId ")
    void changeAvailableState (@Param("productId") UUID productId, @Param("deletedDate")LocalDate deletedDate, @Param("isAvailable") boolean isAvailable);
    @Query("SELECT p FROM Product p WHERE p.id = :productId AND p.ownerGroup = :groupId")
    Product findProductByProductIdAndGroupId(@Param("productId") UUID productId, @Param("groupId") Long groupId);
    @Query("SELECT p FROM Product p WHERE p.isAvailable = false AND p.deletedDate > :comparableDate ")
    List<Product> listOfNotAvailableProducts(@Param("comparableDate") LocalDate comparableData);

}
