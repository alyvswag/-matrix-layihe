package org.example.demo13213.service.product;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.example.demo13213.exception.BaseException;
import org.example.demo13213.model.dao.*;
import org.example.demo13213.model.dto.response.product.ProductResponseDetails;
import org.example.demo13213.repo.coupon.UserCouponRepo;
import org.example.demo13213.repo.product.ProductInventoryRepo;
import org.example.demo13213.repo.product.ProductRepo;
import org.example.demo13213.repo.review.ReviewRepo;
import org.example.demo13213.security.UserPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.example.demo13213.model.dto.enums.response.ErrorResponseMessages.NOT_FOUND;
import static org.example.demo13213.model.dto.enums.response.ErrorResponseMessages.PRODUCT_OUT_OF_STOCK;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    final ProductRepo productRepo;
    final ProductInventoryRepo productInventoryRepo;
    final ReviewRepo reviewRepo;
    final UserCouponRepo  userCouponRepo;

    @Override
    public List<Products> searchProduct(String productName) {
        return productRepo.searchProductsByName(productName);
    }

    @Override
    public ProductResponseDetails getProductDetails(Long productId) {
        //producctun tapilmasi
        Products product = productRepo.findByIdForProduct(productId).orElseThrow(
                ()-> BaseException.notFound("product",productId.toString(),productId)//burani duzelt
        );
        //stock yoxlanmasi
        ProductInventory productQuantity = productInventoryRepo.findByIdForProductQuantity(productId).orElseThrow(
                ()-> BaseException.of(PRODUCT_OUT_OF_STOCK)
        );

        //review tapilmasi
        List<Reviews> reviews = reviewRepo.findByProductIdForReview(productId);
        //ortalama reytinq cixarilmasi
        double avgRating = 0;
        if (!reviews.isEmpty()) {
            avgRating = reviews.stream()
                    .mapToInt(Reviews::getRating)
                    .average()
                    .orElse(0);
        }


        //istifaceinintaninmas
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        //user kuponun alinmasi
        List<UserCoupons> coupons = userCouponRepo.findActiveByUserIdForUserCoupon(userPrincipal.getId());

        //Məhsulun final price-ını hesablayırıq
        BigDecimal finalPrice = product.getPrice(); // başlanğıc qiymət
        OffsetDateTime now = OffsetDateTime.now();

        for (UserCoupons userCoupon : coupons) {
            Coupons coupon = userCoupon.getCoupon();

            // a) Category uyğunluğu
            if (coupon.getCategory() != null && coupon.getCategory().getId().equals(product.getCategory().getId())) {

                // b) Kupon aktivliyi zamanı yoxlanılır
                OffsetDateTime activatedAt = userCoupon.getActivatedAt();
                int activeDays = coupon.getActiveDays() != null ? coupon.getActiveDays() : 0;
                OffsetDateTime expiryDate = activatedAt.plusDays(activeDays);

                if (now.isBefore(expiryDate)) {
                    // c) Endirimi tətbiq et
                    finalPrice = finalPrice.subtract(coupon.getDiscountValue());
                }
            }
        }

        // Qiymət sıfırdan aşağı düşməsin
        if (finalPrice.compareTo(BigDecimal.ZERO) < 0) {
            finalPrice = BigDecimal.ZERO;
        }

        // 7️⃣ Cavab obyektini doldururuq
        ProductResponseDetails response = new ProductResponseDetails();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setPrice(product.getPrice());
        response.setFinalPrice(finalPrice);
        response.setInStock(productQuantity.getQuantity() > 0);
        response.setAvgRating(avgRating);
        response.setCategory(product.getCategory().getName());

        return response;
    }

}