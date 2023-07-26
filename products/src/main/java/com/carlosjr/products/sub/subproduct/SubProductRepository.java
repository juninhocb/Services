package com.carlosjr.products.sub.subproduct;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubProductRepository extends JpaRepository<SubProduct, Long> {

    Optional<SubProduct> findSubProductByName(String name);

}
