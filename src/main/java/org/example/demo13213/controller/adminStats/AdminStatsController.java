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
    public BaseResponse<OverviewResponse> getOverview() {
        return BaseResponse.success(adminStatsService.getOverview());
    }

    @GetMapping("/top-products")
    public BaseResponse<List<TopProductResponse>> getTopProducts() {
        return BaseResponse.success(adminStatsService.getTopProducts());
    }

    @GetMapping("/order-status")
    public BaseResponse<List<OrderStatusResponse>> getOrderStatusStats() {
        return BaseResponse.success(adminStatsService.getOrderStatusStats());
    }

    @GetMapping("/most-reviewed")
    public BaseResponse<List<TopProductResponse>> getMostReviewedProducts() {
        return BaseResponse.success(adminStatsService.getMostReviewedProducts());
    }
}

