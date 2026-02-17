package org.example.demo13213.model.dto.response.adminStats;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class OverviewResponse {
    private Long totalOrders;
    private BigDecimal totalRevenue;
    private Long totalCustomers;
}