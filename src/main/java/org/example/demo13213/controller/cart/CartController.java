package org.example.demo13213.controller.cart;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.demo13213.model.dao.Products;
import org.example.demo13213.model.dto.request.login.LoginRequestPayload;
import org.example.demo13213.model.dto.request.login.UserRequestCreate;
import org.example.demo13213.model.dto.response.base.BaseResponse;
import org.example.demo13213.model.dto.response.cart.CartResponse;
import org.example.demo13213.model.dto.response.catalog.HomeCatalogResponse;
import org.example.demo13213.model.dto.response.login.LoginResponse;


import org.example.demo13213.model.dto.response.product.ProductResponseDetails;
import org.example.demo13213.service.cart.CartService;
import org.example.demo13213.service.catalog.CatalogService;
import org.example.demo13213.service.product.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CartController {

    final CartService cartService;

    @GetMapping
    public BaseResponse<CartResponse> getCart() {
        return BaseResponse.success(cartService.getUserCart());
    }
}