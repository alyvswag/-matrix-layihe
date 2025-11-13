package org.example.demo13213.service.catalog;

import org.example.demo13213.model.dao.Products;

import java.util.List;

public interface CatalogService {
    List<Products> findBrandsWithProducts(Long brandId);
}
