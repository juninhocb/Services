package com.carlosjr.products.products;

import com.carlosjr.products.producttype.ProductType;
import com.carlosjr.products.subproduct.SubProduct;
import com.carlosjr.products.unittype.UnitType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="product")
@Builder
public class Product implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    @GenericGenerator(name = "UUID")
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, columnDefinition = "VARCHAR(36)", updatable = false, nullable = false)
    private UUID id;
    @Column(name = "owner_group")
    private Long ownerGroup;
    private String name;
    private Double value;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "product_type")
    private ProductType productType;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "sub_product")
    private SubProduct subProduct;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "unit_type")
    private UnitType unitType;
    @Column(name="marketplace")
    private String marketPlaceName;
    @CreationTimestamp
    @Column(name = "creation_date")
    private Timestamp creationDate;
    @Column(name = "available_resource")
    private Boolean isAvailable;
    @Column(name = "deleted_date")
    private LocalDate deletedDate;

}
