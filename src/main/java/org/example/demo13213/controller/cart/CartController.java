package org.example.demo13213.controller.cart;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.demo13213.model.dao.CartItems;
import org.example.demo13213.model.dto.response.base.BaseResponse;


import org.example.demo13213.model.dto.response.cart.ProductCouponResponse;
import org.example.demo13213.service.cart.CartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/")
    public BaseResponse<List<CartItems>> getCart() {
        return BaseResponse.success(cartService.getUserCart());
    }

    @PostMapping("/items/")
    public BaseResponse<Void> addCart(@RequestParam Long productId) {
        cartService.addCart(productId);
        return BaseResponse.success();
    }

    @PostMapping("/apply-coupon/")
    public BaseResponse<List<ProductCouponResponse>> addCart(@RequestParam String coupon) {
        return BaseResponse.success(cartService.applyCoupon(coupon));
    }

}