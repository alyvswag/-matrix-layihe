package org.example.demo13213.repo.order;

import lombok.NonNull;
import org.example.demo13213.model.dao.OrderItems;
import org.example.demo13213.model.dao.UserCoupons;
import org.example.demo13213.model.dto.enums.order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItems, Long> {
    @Query("""
        SELECT oi.product.id, SUM(oi.quantity) as totalSold
        FROM OrderItems oi
        GROUP BY oi.product.id
        ORDER BY totalSold DESC
    """)
    List<Object[]> findBestSellingProducts();

    @Query("select oi from OrderItems  oi where oi.order.id=:orderId")
    List<OrderItems> findByOrderItemForOrderId(@Param("orderId") Long orderId);

    List<OrderItems> findAllByProduct_IsActive(Boolean productIsActive);

}

