package com.carlosjr.products.sub.producttype;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface ProductTypeRepository extends JpaRepository<ProductType, Long> {
}
