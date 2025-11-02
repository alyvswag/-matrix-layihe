package org.example.demo13213.controller.auth;



import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.demo13213.model.dto.request.login.LoginRequestPayload;
import org.example.demo13213.model.dto.request.login.UserRequestCreate;
import org.example.demo13213.model.dto.response.base.BaseResponse;
import org.example.demo13213.model.dto.response.login.LoginResponse;

import org.example.demo13213.service.auth.AuthService;
import org.example.demo13213.service.job.FileUploadService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/auth")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthController {

    final AuthService authService;
    final FileUploadService fileUploadService;

    @PostMapping("/login")
    public BaseResponse<LoginResponse> login(@RequestBody LoginRequestPayload loginRequestPayload) {
        return BaseResponse.success(authService.login(loginRequestPayload));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register-user")
    public BaseResponse<LoginResponse> registerUser(@RequestBody UserRequestCreate userRequestCreate) {
        return BaseResponse.success(authService.registerUser(userRequestCreate));
    }

    @PostMapping("/test-auth")
    public BaseResponse<String> test() {
        return BaseResponse.success("salammm");
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/no-auth")
    public BaseResponse<Object> uploadFile(@RequestParam("file") MultipartFile file) {
        return BaseResponse.success(fileUploadService.analyzeUploadedFile(file));
    }


}