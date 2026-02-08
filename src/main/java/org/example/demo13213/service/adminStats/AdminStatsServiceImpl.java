package org.example.demo13213.service.adminStats;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.demo13213.exception.BaseException;
import org.example.demo13213.model.dao.ProductInventory;
import org.example.demo13213.model.dao.Products;
import org.example.demo13213.model.dto.response.catalog.HomeCatalogResponse;
import org.example.demo13213.repo.product.ProductInventoryRepo;
import org.example.demo13213.repo.product.ProductRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class AdminStatsServiceImpl implements AdminStatsService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public Map<String, Object> getOverview() {

        Map<String, Object> response = new HashMap<>();

        response.put("totalOrders", orderRepository.count());
        response.put("totalRevenue",
                Optional.ofNullable(orderRepository.sumPaidOrdersRevenue())
                        .orElse(BigDecimal.ZERO)
        );
        response.put("activeProducts", productRepository.countByIsActiveTrue());
        response.put("averageProductRating",
                Optional.ofNullable(reviewRepository.getAverageRating())
                        .orElse(0.0)
        );

        return response;
    }

    @Override
    public List<Map<String, Object>> getTopProducts() {
        return orderRepository.findTopSellingProducts();
    }

    @Override
    public List<Map<String, Object>> getOrderStatusStats() {
        return orderRepository.countOrdersByStatus();
    }
}

