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

        log.info("➡ Fetching cart items for userId={}", user.getId());

        List<CartItems> items = cartItemRepo.findByUserIdForCartItem(user.getId());

        log.info("✔ Cart items fetched successfully. count={}", items.size());

        return items;
    }


    @Override
    public void addCart(Long productId) {

        log.info("➡ Add to cart request received. productId={}", productId);

        Products product = productRepo.findByIdForProduct(productId).orElseThrow(() -> {
            log.error("❌ Product not found. productId={}", productId);
            return BaseException.notFound("product", productId.toString(), productId);
        });

        log.debug("✔ Product found. id={}, name={}", product.getId(), product.getName());

        UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        log.debug("➡ Fetching cart for userId={}", user.getId());

        Carts cart = cartRepo.findByUserIdForCarts(user.getId())
                .orElseThrow(() -> {
                    log.error("❌ Cart not found for userId={}", user.getId());
                    return BaseException.notFound("cart", user.getId().toString(), "user");
                });

        log.debug("✔ Cart found. cartId={}", cart.getId());

        CartItems ci = new CartItems();
        ci.setProduct(product);
        ci.setCart(cart);
        cartItemRepo.save(ci);

        log.info("✔ Product added to cart successfully. cartId={}, productId={}", cart.getId(), productId);
    }

    @Override
    public List<ProductCouponResponse> applyCoupon(String couponCode) {

        log.info("➡ Apply coupon request received. couponCode={}", couponCode);

        Coupons coupon = couponRepo.findCoupons(couponCode, true)
                .orElseThrow(() -> {
                    log.error("❌ Coupon not found or inactive. code={}", couponCode);
                    return BaseException.notFound("coupon", couponCode, "coupon");
                });

        log.debug("✔ Coupon loaded. id={}, discount={}", coupon.getId(), coupon.getDiscountValue());

        List<CartItems> cartItems = getUserCart();

        log.debug("➡ Cart items loaded for coupon apply. count={}", cartItems.size());

        Long couponCategoryId = coupon.getCategory() != null
                ? coupon.getCategory().getId()
                : null;

        log.debug("➡ Coupon category = {}", couponCategoryId);

        List<ProductCouponResponse> result = cartItems.stream()
                .filter(ci -> {
                    if (couponCategoryId == null) return true;

                    return ci.getProduct() != null
                            && ci.getProduct().getCategory() != null
                            && Objects.equals(
                            ci.getProduct().getCategory().getId(),
                            couponCategoryId
                    );

                    log.trace("Checking productId={} matches couponCategoryId={} -> {}",
                            ci.getProduct().getId(),
                            couponCategoryId,
                            match
                    );

                    return match;
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

                    log.debug("Calculated price for productId={} : original={}, discount={}, final={}",
                            p.getId(), price, discount, finalPrice);

                    return ProductCouponResponse.builder()
                            .p(p)
                            .discValue(discount)
                            .finalPrice(finalPrice)
                            .build();
                })
                .toList();

        if (result.isEmpty()) {
            log.error("❌ Coupon not applicable. code={}", couponCode);
            throw BaseException.of(COUPON_NOT_APPLICABLE);
        }

        log.info("✔ Coupon applied successfully. couponCode={}, affectedProducts={}", couponCode, result.size());

        return result;
    }
}