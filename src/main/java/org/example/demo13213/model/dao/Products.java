package org.example.demo13213.model.dao;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "products",
        uniqueConstraints = {},
        indexes = {})
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    UUID id;

    @Column(nullable = false, length = 255)
    String name;

    @Column(length = 500)
    String shortDescription;

    @Column(columnDefinition = "TEXT")
    String description;

    @ManyToOne
    @JoinColumn(name = "brand_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_products_brand"))
    Brands brand;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_products_category"))
    Categories category;

    @Column(nullable = false, precision = 10, scale = 2)
    BigDecimal price;

    @Column(nullable = false)
    Boolean isActive = true;

    @Column(nullable = false)
    Boolean isVegan = false;

    @Column(nullable = false)
    Boolean isForSensitiveSkin = false;

    @Column(length = 30)
    String skinType;

    @Column(length = 30)
    String concernType;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    OffsetDateTime updatedAt;
}
