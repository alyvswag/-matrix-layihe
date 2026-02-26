package org.example.demo13213.service.adminStats;

import org.example.demo13213.model.dto.enums.order.OrderStatus;
import org.example.demo13213.model.dto.response.adminStats.OrderStatusResponse;
import org.example.demo13213.model.dto.response.adminStats.OverviewResponse;
import org.example.demo13213.model.dto.response.adminStats.TopProductResponse;

import java.util.List;

public interface AdminStatsService {
    OverviewResponse getOverview();

    List<TopProductResponse> getTopProducts();

    OrderStatusResponse getOrderStatusStats(OrderStatus orderStatus);

    List<TopProductResponse> getMostReviewedProducts();
}