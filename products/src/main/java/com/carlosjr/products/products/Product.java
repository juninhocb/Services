package com.carlosjr.products.products;

import com.carlosjr.products.products.enums.FeedSubProduct;
import com.carlosjr.products.products.enums.ProductType;
import com.carlosjr.products.products.enums.UnitType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="product")
public class Product implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double value;
    @Enumerated(EnumType.STRING)
    @Column(name="product_type")
    private ProductType productType;
    @Enumerated(EnumType.STRING)
    @Column(name="feed_sub_product")
    private FeedSubProduct FeedSubProduct;
    @Enumerated(EnumType.STRING)
    @Column(name="unit_type")
    private UnitType unitType;
    @Column(name="marketplace")
    private String marketPlaceName;
    @Column(name = "creation_date")
    private String creation_date;

}
