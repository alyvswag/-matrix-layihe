package org.example.demo13213.model.dao;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "user_coupons", uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_coupons_user_coupon", columnNames = {"user_id", "coupon_id"})
})
public class UserCoupons {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_user_coupons_user"))
    Users user;

    @ManyToOne
    @JoinColumn(name = "coupon_id", referencedColumnName = "id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_user_coupons_coupon"))
    Coupons coupon;

    @Column(name = "activated_at", nullable = false)
    OffsetDateTime activatedAt;

    @Column(name = "expires_at", nullable = false)
    OffsetDateTime expiresAt;

    @Column(name = "is_active", nullable = false)
    Boolean isActive = true;

    @ManyToOne
    @JoinColumn(name = "used_order_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_user_coupons_used_order"))
    Orders usedOrder;

    @Column(name = "used_at")
    OffsetDateTime usedAt;
}
