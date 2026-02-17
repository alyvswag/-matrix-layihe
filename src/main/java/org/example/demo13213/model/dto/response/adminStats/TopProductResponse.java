package org.example.demo13213.model.dto.response.adminStats;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopProductResponse {
    private String name;
    private Integer soldQuantity;
}