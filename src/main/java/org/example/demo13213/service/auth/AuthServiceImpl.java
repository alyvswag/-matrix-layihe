package org.example.demo13213.service.auth;



import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import lombok.extern.slf4j.Slf4j;
import org.example.demo13213.exception.BaseException;
import org.example.demo13213.model.dao.Carts;
import org.example.demo13213.model.dao.Users;
import org.example.demo13213.model.dto.request.login.LoginRequestPayload;
import org.example.demo13213.model.dto.request.login.UserRequestCreate;
import org.example.demo13213.model.dto.response.login.LoginResponse;
import org.example.demo13213.repo.cart.CartRepo;
import org.example.demo13213.repo.user.UserRepo;
import org.example.demo13213.security.jwt.TokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.example.demo13213.model.dto.enums.response.ErrorResponseMessages.INVALID_USERNAME_OR_PASSWORD;
import static org.example.demo13213.model.dto.enums.response.ErrorResponseMessages.USERNAME_ALREADY_REGISTERED;
import static org.example.demo13213.utils.CommonUtils.throwIf;
import org.springframework.security.core.AuthenticationException;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl  implements AuthService{


    final AuthenticationManager authenticationManager;
    final UserRepo userRepo;
    final CartRepo cartRepo;
    final PasswordEncoder passwordEncoder;
    final TokenProvider tokenProvider;
    final UserDetailsService userDetailsService;

    @Override
    public LoginResponse registerUser(UserRequestCreate userRequestCreate) {
        throwIf(
                //1ci hisse checker
                () -> userRepo.findUserByUsername(userRequestCreate.getUsername()).isPresent(),//optional<user1>.is
                //2ci hisse exception
                BaseException.of(USERNAME_ALREADY_REGISTERED)
        );
        //mapper islemedi manual mapping etdik
        Users users = new Users();
        users.setUsername(userRequestCreate.getUsername());
        users.setPassword(passwordEncoder.encode(userRequestCreate.getPassword()));
        users.setFullName(userRequestCreate.getFullName());
        users.setIsActive(true);
        userRepo.save(users);
        //todo:  mutleq user yaradan kimi carts yarat
        Carts carts  = new Carts();
        carts.setUser(users);
        cartRepo.save(carts);
        return prepareLoginResponse(userRequestCreate.getUsername());
    }

    @Override
    public LoginResponse login(LoginRequestPayload payload) {
        authenticate(payload);//bu metod pass ve useri yoxlayir
        return prepareLoginResponse(payload.getUsername());
    }

    @Override
    public LoginResponse refreshToken(String refreshToken) {
        String username = tokenProvider.getUsername(refreshToken); //nuray1
        return prepareLoginResponse(username);
    }
    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
    }

    @Override
    public void setAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
        );
//istifadecinin kim oldugun saxlanilr
    }


//private classlar

private LoginResponse prepareLoginResponse(String username) {

    Users users = findUserByUser(username);//talib1
    String accessToken = tokenProvider.generate(users).get(0);
    String refreshToken = tokenProvider.generate(users).get(1);

    return LoginResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();

}
private Users findUserByUser(String u) {
    return userRepo.findUserByUsername(u)
            .orElseThrow(
                    () -> BaseException.notFound(Users.class.getSimpleName(), "user", u)
            );
}
        private void authenticate(LoginRequestPayload request) {
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
                );
            } catch (AuthenticationException e) {
                throw e.getCause() instanceof BaseException ?
                        (BaseException) e.getCause() :
                        BaseException.of(INVALID_USERNAME_OR_PASSWORD);//user ve yaxud parol yanlisdir
            }
        }

}


