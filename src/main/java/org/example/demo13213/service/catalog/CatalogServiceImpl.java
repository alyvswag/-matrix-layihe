package org.example.demo13213.service.catalog;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.demo13213.exception.BaseException;
import org.example.demo13213.model.dao.Products;
import org.example.demo13213.model.dto.response.catalog.HomeCatalogResponse;
import org.example.demo13213.repo.product.ProductRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        log.info("➡️ Request received: findBrandsWithProducts brandId={}", brandId);
        List<Products> products = productRepo.findByBrandId(brandId);

        if (products.isEmpty()) {
            log.warn("⚠️ No products found for brandId={}", brandId);
            throw BaseException.notFound("brand", "id", brandId.toString());
        }
        log.info("✅ Found {} products for brandId={}", products.size(), brandId);
        return products;
    }//men yazdim sende yaz

    @Override
    public HomeCatalogResponse getHomeCatalog(int page, int size) {

        // 1) New arrivals üçün: createdAt-a görə sort lazımdır
        Pageable newArrivalsPageable =
                PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Products> newArrivals = productRepo.findNewArrivals(newArrivalsPageable);

        // 2) Best sellers üçün: sort artıq JPQL-in içində var, əlavə sort lazım deyil
        Pageable bestSellersPageable =
                PageRequest.of(page, size); // Sort YOXDUR

        Page<Products> bestSellersPage = productRepo.findBestSellers(bestSellersPageable);

        return HomeCatalogResponse.builder()
                .newArrivals(newArrivals.getContent())
                .bestSellers(bestSellersPage.getContent())
                .build();
    }
}
