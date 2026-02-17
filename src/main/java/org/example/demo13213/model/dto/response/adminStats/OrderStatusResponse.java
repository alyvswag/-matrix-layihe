package org.example.demo13213.model.dto.response.adminStats;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderStatusResponse {
    private String status;
    private Long count;
}