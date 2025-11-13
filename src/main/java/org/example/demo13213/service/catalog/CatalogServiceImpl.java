package org.example.demo13213.service.catalog;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.demo13213.exception.BaseException;
import org.example.demo13213.model.dao.Products;
import org.example.demo13213.repo.product.ProductRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class CatalogServiceImpl implements CatalogService {
   final ProductRepo productRepo;






    @Override
    public List<Products> findBrandsWithProducts(Long brandId) {
        log.info("Searching active products for brandId: {}", brandId);

        List<Products> products = productRepo.findByBrandId(brandId);

        if (products.isEmpty()) {
            throw BaseException.notFound("brand", "id", brandId.toString());
        }

        return products;
    }
}
