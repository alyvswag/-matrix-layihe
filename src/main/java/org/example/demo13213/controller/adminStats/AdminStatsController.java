package org.example.demo13213.controller.adminStats;


import lombok.RequiredArgsConstructor;
import org.example.demo13213.model.dao.OrderItems;
import org.example.demo13213.service.adminStats.AdminStatsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/stats")
@RequiredArgsConstructor
public class AdminStatsController {

    private final AdminStatsService adminStatsService;

    @GetMapping("/overview")
    public Map<String, Object> getOverview() {
        return adminStatsService.getOverview();
    }

    @GetMapping("/top-products")
    public List<OrderItems> getTopProducts() {
        return adminStatsService.getTopProducts();
    }

    @GetMapping("/order-status")
    public List<Map<String, Object>> getOrderStatusStats() {
        return adminStatsService.getOrderStatusStats();
    }
}

