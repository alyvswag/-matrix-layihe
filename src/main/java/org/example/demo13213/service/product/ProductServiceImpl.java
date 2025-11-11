package org.example.demo13213.service.product;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.demo13213.model.dao.Products;
import org.example.demo13213.model.dto.response.product.ProductResponseDetails;
import org.example.demo13213.repo.product.ProductRepo;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    final ProductRepo productRepo;

    @Override
    public List<Products> searchProduct(String productName) {
        return productRepo.searchProductsByName(productName);
    }

    @Override
    public ProductResponseDetails getProductDetails(Long productId) {
        //tokenin icinden istfadecin kim oldugun gotur sora istifadecini
        //usercupon istifadecinin endirimleri varmi
        // varsa tetbiq edeceksen hemin mehsula ve real qiymetin dondureceksen
        //gedeceksen review cedvelinden hemin product id ye uygun olan mehsulu tapib getirib onlarinda
        //jsona vurub yeniden geri donderesen
        ProductResponseDetails p = new ProductResponseDetails();
        p.setId();
        p.setFinalPrice();

        return p;
    }

}
