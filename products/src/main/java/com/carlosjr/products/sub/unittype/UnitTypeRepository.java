package com.carlosjr.products.sub.unittype;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnitTypeRepository extends JpaRepository<UnitType, Long> {

    Optional<UnitType> findUnitTypeByName(String name);

}
