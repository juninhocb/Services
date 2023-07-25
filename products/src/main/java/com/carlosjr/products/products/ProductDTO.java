package com.carlosjr.products.products;

import com.carlosjr.products.producttype.ProductType;
import com.carlosjr.products.subproduct.SubProduct;
import com.carlosjr.products.unittype.UnitType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
public record ProductDTO(
        @NotNull
        @Positive
        Long ownerGroup,
        @NotBlank
        @Pattern(regexp = "^[a-zA-ZÀ-ÿ]+$", message = "Name should only contain letters")
        String name,
        @NotNull
        @Positive
        Double value,
        @NotNull
        ProductType productType,
        SubProduct feedSubProduct,
        @NotNull
        UnitType unitType,
        @NotBlank
        @Pattern(regexp = "^[a-zA-ZÀ-ÿ]+$", message = "Marketplace should only contain letters")
        String marketPlaceName) {
}
