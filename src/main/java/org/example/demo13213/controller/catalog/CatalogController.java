package org.example.demo13213.controller.catalog;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.demo13213.model.dao.Products;
import org.example.demo13213.model.dto.response.base.BaseResponse;
import org.example.demo13213.model.dto.response.catalog.HomeCatalogResponse;
import org.example.demo13213.service.catalog.CatalogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalog")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CatalogController {

    final CatalogService catalogService;

    @GetMapping("/brands-with-products/{brandId}")
    public BaseResponse<List<Products>> findBrandWithProducts(@PathVariable Long brandId) {
        return BaseResponse.success(catalogService.findBrandsWithProducts(brandId));
    }

    @GetMapping("/home")
    public BaseResponse<HomeCatalogResponse> getHomeCatalog(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return BaseResponse.success(catalogService.getHomeCatalog(page, size));
    }
}