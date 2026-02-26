package org.example.demo13213.model.dto.response.catalog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.demo13213.model.dao.Products;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HomeCatalogResponse {
    List<Products> newArrivals;
    List<Products> bestSellers;
}