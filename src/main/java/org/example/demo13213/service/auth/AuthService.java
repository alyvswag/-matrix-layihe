package org.example.demo13213.service.auth;

import org.example.demo13213.model.dto.request.login.LoginRequestPayload;
import org.example.demo13213.model.dto.request.login.UserRequestCreate;
import org.example.demo13213.model.dto.response.login.LoginResponse;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthService {
    LoginResponse registerUser(@RequestBody UserRequestCreate userRequestCreate);

    LoginResponse login(@RequestBody LoginRequestPayload payload);

    LoginResponse refreshToken(String refreshToken);

    void logout();

    void setAuthentication(String user);



}
