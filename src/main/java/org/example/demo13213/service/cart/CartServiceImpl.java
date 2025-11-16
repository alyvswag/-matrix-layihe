package org.example.demo13213.service.cart;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.demo13213.exception.BaseException;
import org.example.demo13213.model.dao.CartItems;
import org.example.demo13213.model.dao.Carts;
import org.example.demo13213.model.dto.response.cart.CartItemResponse;
import org.example.demo13213.model.dto.response.cart.CartResponse;
import org.example.demo13213.repo.cart.CartItemRepo;
import org.example.demo13213.repo.cart.CartRepo;
import org.example.demo13213.security.UserPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepo cartRepo;
    private final CartItemRepo cartItemRepo;

    @Override
    public CartResponse getUserCart() {

        UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Long userId = user.getId();

        Carts cart = cartRepo.findByUserId(userId)
                .orElseThrow(() -> BaseException.notFound("Cart not found"));

        List<CartItems> items = cartItemRepo.findByCartId(cart.getId());

        List<CartItemResponse> itemResponses = items.stream()
                .map(i -> CartItemResponse.builder()
                        .productId(i.getProduct().getId())
                        .productName(i.getProduct().getName())
                        .price(i.getProduct().getPrice())
                        .quantity(1)
                        .build())
                .toList();

        return CartResponse.builder()
                .cartId(cart.getId())
                .items(itemResponses)
                .build();
    }
}