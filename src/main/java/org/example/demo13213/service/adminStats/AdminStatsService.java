package org.example.demo13213.service.adminStats;

import org.example.demo13213.model.dto.response.adminStats.OverviewResponse;
import org.example.demo13213.model.dto.response.adminStats.TopProductResponse;
import org.example.demo13213.model.dto.response.adminStats.OrderStatusResponse;
import java.util.List;

public interface AdminStatsService {
    OverviewResponse getOverview();
    List<TopProductResponse> getTopProducts();
    List<OrderStatusResponse> getOrderStatusStats();
    List<TopProductResponse> getMostReviewedProducts();
}