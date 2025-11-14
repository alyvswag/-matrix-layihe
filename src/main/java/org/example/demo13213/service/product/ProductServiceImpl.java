package org.example.demo13213.service.product;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.example.demo13213.exception.BaseException;
import org.example.demo13213.model.dao.*;
import org.example.demo13213.model.dto.request.product.ProductFilterRequest;
import org.example.demo13213.model.dto.response.product.ProductResponseDetails;
import org.example.demo13213.repo.coupon.UserCouponRepo;
import org.example.demo13213.repo.order.OrderItemRepo;
import org.example.demo13213.repo.product.ProductInventoryRepo;
import org.example.demo13213.repo.product.ProductRepo;
import org.example.demo13213.repo.review.ReviewRepo;
import org.example.demo13213.security.UserPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
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
    final UserCouponRepo userCouponRepo;
    final OrderItemRepo orderItemRepo;
    final EntityManager em;

    @Override
    public List<Products> searchProduct(String productName) {
        return productRepo.searchProductsByName(productName);
    }

    @Override
    public ProductResponseDetails getProductDetails(Long productId) {
        //producctun tapilmasi
        Products product = productRepo.findByIdForProduct(productId).orElseThrow(
                () -> BaseException.notFound("product", productId.toString(), productId)//burani duzelt
        );
        //stock yoxlanmasi
        ProductInventory productQuantity = productInventoryRepo.findByIdForProductQuantity(productId).orElseThrow(
                () -> BaseException.of(PRODUCT_OUT_OF_STOCK)
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

    @Override
    public List<Products> getBestSellers() {
        List<Object[]> bestSellersRaw = orderItemRepo.findBestSellingProducts();

        List<Long> productIds = bestSellersRaw.stream()
                .map(obj -> (Long) obj[0])
                .toList();

        if (productIds.isEmpty()) {
            return List.of();
        }

        List<Products> products = productRepo.findAllById(productIds);

        products.sort(Comparator.comparingInt((Products p) -> {
            Long pid = p.getId();
            Object[] match = bestSellersRaw.stream()
                    .filter(obj -> obj[0].equals(pid))
                    .findFirst()
                    .orElse(null);
            return match == null ? 0 : ((Number) match[1]).intValue();
        }).reversed());

        return products;
    }

    @Override
    public List<Products> filter(ProductFilterRequest productFilterRequest) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Products> query = cb.createQuery(Products.class);
        Root<Products> root = query.from(Products.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.isTrue(root.get("isActive")));

        if (productFilterRequest.getMinPrice() != null) {
            predicates.add(
                    cb.greaterThanOrEqualTo(
                            root.get("price"),
                            productFilterRequest.getMinPrice()
                    )
            );
        }
        if (productFilterRequest.getMaxPrice() != null) {
            predicates.add(
                    cb.lessThanOrEqualTo(
                            root.get("price"),
                            productFilterRequest.getMaxPrice()
                    )
            );
        }

        if (productFilterRequest.getIsVegan() != null) {
            predicates.add(
                    cb.equal(
                            root.get("isVegan"),
                            productFilterRequest.getIsVegan()
                    )
            );
        }
        if (productFilterRequest.getIsForSensitiveSkin() != null) {
            predicates.add(
                    cb.equal(
                            root.get("isForSensitiveSkin"),
                            productFilterRequest.getIsForSensitiveSkin()
                    )
            );
        }

        if (productFilterRequest.getSkinType() != null) {
            predicates.add(
                    cb.equal(
                            root.get("skinType"),
                            productFilterRequest.getSkinType()
                    )
            );
        }
        if (productFilterRequest.getConcernType() != null) {
            predicates.add(
                    cb.equal(
                            root.get("concernType"),
                            productFilterRequest.getConcernType()
                    )
            );
        }
        //list predicate
        //mehdudiyyetler

        // WHERE
        query.where(cb.and(predicates.toArray(new Predicate[0])));

        TypedQuery<Products> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }


}