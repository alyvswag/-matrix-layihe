package org.example.demo13213.controller.adminStats;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.demo13213.model.dao.CartItems;
import org.example.demo13213.model.dto.response.base.BaseResponse;


import org.example.demo13213.model.dto.response.cart.ProductCouponResponse;
import org.example.demo13213.service.cart.CartService;
import org.springframework.web.bind.annotation.*;

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
    public List<Map<String, Object>> getTopProducts() {
        return adminStatsService.getTopProducts();
    }

    @GetMapping("/order-status")
    public List<Map<String, Object>> getOrderStatusStats() {
        return adminStatsService.getOrderStatusStats();
    }
}

