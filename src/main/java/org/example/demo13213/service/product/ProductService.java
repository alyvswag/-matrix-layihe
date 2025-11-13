package org.example.demo13213.service.product;

import org.example.demo13213.model.dao.Products;
import org.example.demo13213.model.dto.response.product.ProductResponseDetails;

import java.util.List;

public interface ProductService {
    List<Products> searchProduct(String productName);

    ProductResponseDetails getProductDetails(Long productId);

    List<Products> getBestSellers();

}
