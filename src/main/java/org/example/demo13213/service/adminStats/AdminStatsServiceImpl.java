package org.example.demo13213.service.adminStats;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.demo13213.model.dto.response.adminStats.OrderStatusResponse;
import org.example.demo13213.model.dto.response.adminStats.OverviewResponse;
import org.example.demo13213.model.dto.response.adminStats.TopProductResponse;
import org.example.demo13213.repo.order.OrderRepo;
import org.example.demo13213.repo.product.ProductRepo;
import org.example.demo13213.repo.review.ReviewRepo;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class AdminStatsServiceImpl implements AdminStatsService {

    private final OrderRepo orderRepo;
    private final ProductRepo productRepo;
    private final ReviewRepo reviewRepo;

    @Override
    public OverviewResponse getOverview() {
        return orderRepo.getOverviewData();
    }

    @Override
    public List<TopProductResponse> getTopProducts() {
        return productRepo.findTopSellingProducts();
    }

    @Override
    public List<OrderStatusResponse> getOrderStatusStats() {
        return orderRepo.findOrderStatusStats();
    }

    @Override
    public List<TopProductResponse> getMostReviewedProducts() {
        return reviewRepo.getMostReviewedProducts();
    }
}

