package org.example.demo13213.controller.auth;

import lombok.RequiredArgsConstructor;
import org.example.demo13213.model.dto.request.login.LoginRequestPayload;
import org.example.demo13213.model.dto.request.login.UserRequestCreate;
import org.example.demo13213.model.dto.response.base.BaseResponse;
import org.example.demo13213.model.dto.response.login.LoginResponse;
import org.example.demo13213.service.auth.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

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

}