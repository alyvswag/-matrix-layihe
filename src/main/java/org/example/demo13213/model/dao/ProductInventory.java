package org.example.demo13213.model.dao;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "product_inventory")
public class ProductInventory {

    @Id
    @Column(name = "product_id", nullable = false, updatable = false)
    UUID productId;

    @MapsId
    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_product_inventory_product"))
    Products product;

    @Column(nullable = false)
    Integer quantity = 0;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    OffsetDateTime updatedAt;
}
