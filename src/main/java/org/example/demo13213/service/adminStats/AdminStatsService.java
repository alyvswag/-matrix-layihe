package org.example.demo13213.service.adminStats;

import java.util.List;
import java.util.Map;

public interface AdminStatsService {
    Map<String, Object> getOverview();

    List<Map<String, Object>> getTopProducts();

    List<Map<String, Object>> getOrderStatusStats();
}