package org.example.demo13213.repo.order;

import org.example.demo13213.model.dao.Orders;
import org.example.demo13213.model.dao.Products;
import org.example.demo13213.model.dto.response.adminStats.OrderStatusResponse;
import org.example.demo13213.model.dto.response.adminStats.OverviewResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface OrderRepo  extends CrudRepository<Orders, Integer> {
    @Query("Select o From Orders o WHERE o.id=:id AND o.status = 'PENDING' ")
    Optional<Orders> findByIdForOrders(@Param("id") Long id);

    @Query("Select o from Orders o where o.user.id = :id and o.status = 'PAID' ")
    List<Orders> findOrdersByUserId(@Param("id") Long id);

    @Query("SELECT new org.example.demo13213.model.dto.response.adminStats.OverviewResponse(COUNT(o), SUM(o.grandTotal), (SELECT COUNT(u) FROM Users u)) FROM Orders o")
    OverviewResponse getOverviewData();

    @Query("SELECT new org.example.demo13213.model.dto.response.adminStats.OrderStatusResponse(o.status, COUNT(o)) FROM Orders o GROUP BY o.status")
    List<OrderStatusResponse> findOrderStatusStats();
}
