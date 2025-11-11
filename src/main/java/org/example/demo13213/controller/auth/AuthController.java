package org.example.demo13213.controller.auth;



import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.demo13213.model.dao.Products;
import org.example.demo13213.model.dto.request.login.LoginRequestPayload;
import org.example.demo13213.model.dto.request.login.UserRequestCreate;
import org.example.demo13213.model.dto.response.base.BaseResponse;
import org.example.demo13213.model.dto.response.login.LoginResponse;

import org.example.demo13213.service.auth.AuthService;

import org.example.demo13213.service.product.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthController {

    final AuthService authService;

    @PostMapping("/login")
    public BaseResponse<LoginResponse> login(@RequestBody LoginRequestPayload loginRequestPayload) {
        return BaseResponse.success(authService.login(loginRequestPayload));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register-user")
    public BaseResponse<LoginResponse> registerUser(@RequestBody UserRequestCreate userRequestCreate) {
        return BaseResponse.created(authService.registerUser(userRequestCreate));
    }

    @PostMapping("/refresh-token/{refreshToken}")
    public BaseResponse<LoginResponse> refreshToken(@PathVariable("refreshToken") String refreshToken) {
        return BaseResponse.success(authService.refreshToken(refreshToken));
    }

    @PostMapping("/logout")
    public BaseResponse<Void> logout() {
        authService.logout();
        return BaseResponse.success();
    }

    //test ucun

    @PostMapping("/test-auth")
    public BaseResponse<String> test() {
        return BaseResponse.success("salammm");
    }

}