package org.example.demo13213.controller.adminStats;


import lombok.RequiredArgsConstructor;
import org.example.demo13213.model.dto.enums.order.OrderStatus;
import org.example.demo13213.model.dto.response.adminStats.OrderStatusResponse;
import org.example.demo13213.model.dto.response.adminStats.OverviewResponse;
import org.example.demo13213.model.dto.response.adminStats.TopProductResponse;
import org.example.demo13213.model.dto.response.base.BaseResponse;
import org.example.demo13213.service.adminStats.AdminStatsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public BaseResponse<OrderStatusResponse> getOrderStatusStats(@RequestParam OrderStatus orderStatus) {
        return BaseResponse.success(adminStatsService.getOrderStatusStats(orderStatus));
    }

    @GetMapping("/most-reviewed")
    public BaseResponse<List<TopProductResponse>> getMostReviewedProducts() {
        return BaseResponse.success(adminStatsService.getMostReviewedProducts());
    }
}

