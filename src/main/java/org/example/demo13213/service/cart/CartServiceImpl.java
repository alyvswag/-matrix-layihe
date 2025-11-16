package org.example.demo13213.service.cart;

import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.demo13213.exception.BaseException;
import org.example.demo13213.model.dao.CartItems;
import org.example.demo13213.model.dao.Carts;
import org.example.demo13213.model.dao.Coupons;
import org.example.demo13213.model.dao.Products;
import org.example.demo13213.model.dto.response.cart.ProductCouponResponse;
import org.example.demo13213.repo.cart.CartItemRepo;
import org.example.demo13213.repo.cart.CartRepo;
import org.example.demo13213.repo.coupon.CouponRepo;
import org.example.demo13213.repo.product.ProductRepo;
import org.example.demo13213.security.UserPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.example.demo13213.model.dto.enums.response.ErrorResponseMessages.COUPON_NOT_APPLICABLE;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    final CartRepo cartRepo;
    final CartItemRepo cartItemRepo;
    final ProductRepo productRepo;
    final CouponRepo couponRepo;


    @Override
    public List<CartItems> getUserCart() {
        UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return cartItemRepo.findByUserIdForCartItem(user.getId());

    }

    @Override
    public void addCart(Long productId) {
        Products product = productRepo.findByIdForProduct(productId).orElseThrow(
                () -> BaseException.notFound("product", productId.toString(), productId)//burani duzelt
        );
        UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Carts cart = cartRepo.findByUserIdForCarts(user.getId())
                .orElseThrow(() -> BaseException.notFound("cart", user.getId().toString(), "user"));
        CartItems ci = new CartItems();
        ci.setProduct(product);
        ci.setCart(cart);
        cartItemRepo.save(ci);
    }

    @Override
    public List<ProductCouponResponse> applyCoupon(String couponCode) {

        Coupons coupon = couponRepo.findCoupons(couponCode, true)
                .orElseThrow(() -> BaseException.notFound("coupon", couponCode, "coupon"));

        List<CartItems> cartItems = getUserCart();

        Long couponCategoryId = coupon.getCategory() != null
                ? coupon.getCategory().getId()
                : null;

        List<ProductCouponResponse> result = cartItems.stream()
                .filter(ci -> {
                    if (couponCategoryId == null) return true;

                    return ci.getProduct() != null
                            && ci.getProduct().getCategory() != null
                            && Objects.equals(
                            ci.getProduct().getCategory().getId(),
                            couponCategoryId
                    );
                })
                .map(ci -> {
                    Products p = ci.getProduct();

                    BigDecimal price = p.getPrice();
                    BigDecimal discount = coupon.getDiscountValue() != null
                            ? coupon.getDiscountValue()
                            : BigDecimal.ZERO;

                    BigDecimal finalPrice = price.subtract(discount);
                    if (finalPrice.compareTo(BigDecimal.ZERO) < 0) {
                        finalPrice = BigDecimal.ZERO;
                    }

                    return ProductCouponResponse.builder()
                            .p(p)
                            .discValue(discount)
                            .finalPrice(finalPrice)
                            .build();
                })
                .toList();

        // 🔥 Əsas hissə — uyğun məhsul tapılmayıbsa exception
        if (result.isEmpty()) {
            throw BaseException.of(COUPON_NOT_APPLICABLE);
        }
        return result;
    }



}