package com.carlosjr.products.producttype;

import com.carlosjr.products.sub.producttype.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductTypeRepository extends JpaRepository<ProductType, Long> {

    Optional<ProductType> findProductTypeByName(String name);

}
