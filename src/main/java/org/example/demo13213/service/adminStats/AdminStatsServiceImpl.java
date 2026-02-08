package org.example.demo13213.service.adminStats;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.demo13213.model.dao.OrderItems;
import org.example.demo13213.model.dao.Products;
import org.example.demo13213.model.repsonse.OverviewResponse;
import org.example.demo13213.repo.order.OrderItemRepo;
import org.example.demo13213.repo.order.OrderRepo;
import org.example.demo13213.repo.product.ProductRepo;
import org.example.demo13213.repo.review.ReviewRepo;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class AdminStatsServiceImpl implements AdminStatsService {

    private final OrderRepo orderRepository;
    private final OrderItemRepo orderItemRepo;
    private final ProductRepo productRepository;
    private final ReviewRepo reviewRepository;

    @Override
    public Map<String, Object> getOverview() {


        Double defaultTotalRevenue = 0.0;
        Map<String, Object> response = new HashMap<>();

        response.put("totalOrders", orderRepository.count());
        response.put("totalRevenue",
                Optional.ofNullable(orderRepository.sumPaidOrdersRevenue())
                        .orElse(defaultTotalRevenue)
        );
        response.put("activeProducts", productRepository.countByIsActiveTrue());
        response.put("averageProductRating",
                Optional.ofNullable(reviewRepository.findAverageRating())
                        .orElse(0.0)
        );
        OverviewResponse r = new OverviewResponse();
        r.setTotalOrders(orderRepository.count());
        return r;
    }

    @Override
    public List<OrderItems> getTopProducts() {
        return orderItemRepo.findBestSellingProducts();
    }

    @Override
    public List<Map<String, Object>> getOrderStatusStats() {
        return orderRepository.countOrdersByStatus();
    }
}

