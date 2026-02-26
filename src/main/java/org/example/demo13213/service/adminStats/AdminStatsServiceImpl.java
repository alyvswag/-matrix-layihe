package org.example.demo13213.service.adminStats;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.demo13213.model.dao.OrderItems;
import org.example.demo13213.model.dao.ProductInventory;
import org.example.demo13213.model.dto.enums.order.OrderStatus;
import org.example.demo13213.model.dto.response.adminStats.OrderStatusResponse;
import org.example.demo13213.model.dto.response.adminStats.OverviewResponse;
import org.example.demo13213.model.dto.response.adminStats.TopProductResponse;
import org.example.demo13213.repo.order.OrderItemRepo;
import org.example.demo13213.repo.order.OrderRepo;
import org.example.demo13213.repo.product.ProductInventoryRepo;
import org.example.demo13213.repo.review.ReviewRepo;
import org.example.demo13213.repo.user.UserRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class AdminStatsServiceImpl implements AdminStatsService {

    private final OrderRepo orderRepo;
    private final ReviewRepo reviewRepo;
    private final UserRepo userRepo;
    private final OrderItemRepo orderItemRepo;
    private final ProductInventoryRepo productInventoryRepo;

    @Override
    public OverviewResponse getOverview() {
        long ordersCount = orderRepo.count();
        BigDecimal grandTotalSum = orderRepo.sumGrandTotal();
        long usersCount = userRepo.count();

        return new OverviewResponse(ordersCount, grandTotalSum, usersCount);
    }

    @Override
    public List<TopProductResponse> getTopProducts() {
        List<OrderItems> oi = orderItemRepo.findAllByProduct_IsActive(Boolean.TRUE);
        List<TopProductResponse> list = new ArrayList<>();

        for (OrderItems o : oi) {
            ProductInventory p = productInventoryRepo.findByIdForProductQuantity(o.getProduct().getId()).orElseThrow(
            );
            list.add(new TopProductResponse(p.getProduct().getName(), p.getQuantity()));
        }

        return list;
    }

    @Override
    public OrderStatusResponse getOrderStatusStats(OrderStatus orderStatus) {
        long count = orderRepo.countOrdersByStatus(orderStatus);
        return new OrderStatusResponse(orderStatus.name(), count);
    }

    @Override
    public List<TopProductResponse> getMostReviewedProducts() {
        return reviewRepo.getMostReviewedProducts();
    }
}