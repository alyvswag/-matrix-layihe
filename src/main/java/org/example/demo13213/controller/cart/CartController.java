package org.example.demo13213.controller.cart;

import lombok.RequiredArgsConstructor;
import org.example.demo13213.model.dao.CartItems;
import org.example.demo13213.model.dto.response.base.BaseResponse;
import org.example.demo13213.model.dto.response.cart.ProductCouponResponse;
import org.example.demo13213.service.cart.CartService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @DeleteMapping("/delete-card/{id}")
    public BaseResponse<Void> deleteCart(@PathVariable Long id) {
        cartService.removeCart(id);
        return BaseResponse.success();
    }
}