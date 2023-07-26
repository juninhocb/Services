package com.carlosjr.products.sub.subproduct;


import com.carlosjr.products.products.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "sub_product")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "subProduct")
    @JsonIgnore
    private Set<Product> products;

}
