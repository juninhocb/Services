package com.carlosjr.products.producttype;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface ProductTypeRepository extends JpaRepository<ProductType, Long> {
}
