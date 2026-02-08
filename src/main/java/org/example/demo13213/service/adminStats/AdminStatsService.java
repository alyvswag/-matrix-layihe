package org.example.demo13213.service.adminStats;

import org.example.demo13213.model.dao.OrderItems;

import java.util.List;
import java.util.Map;

public interface AdminStatsService {
    Map<String, Object> getOverview();

    List<OrderItems> getTopProducts();

    List<Map<String, Object>> getOrderStatusStats();
}
