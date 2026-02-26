package org.example.demo13213.controller.product;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.demo13213.model.dao.Products;
import org.example.demo13213.model.dto.request.product.ProductFilterRequest;
import org.example.demo13213.model.dto.response.base.BaseResponse;
import org.example.demo13213.model.dto.response.product.ProductResponseDetails;
import org.example.demo13213.service.product.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductController {

    final ProductService productService;

    @GetMapping("/search/{searchWord}")
    public BaseResponse<List<Products>> search(@PathVariable String searchWord) {
        return BaseResponse.success(productService.searchProduct(searchWord));
    }

    @GetMapping("/{id}/details")
    public BaseResponse<ProductResponseDetails> getProductDetails(@PathVariable Long id) {
        return BaseResponse.success(productService.getProductDetails(id));
    }

    @GetMapping("/bestsellers")
    public BaseResponse<List<Products>> getBestSellers() {
        return BaseResponse.success(productService.getBestSellers());
    }

    @PostMapping("/filter-products")
    public BaseResponse<List<Products>> filterProducts(@RequestBody ProductFilterRequest productFilterRequest) {
        return BaseResponse.success(productService.filter(productFilterRequest));
    }
}