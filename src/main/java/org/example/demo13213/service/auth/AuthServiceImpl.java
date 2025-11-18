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
public class AuthServiceImpl implements AuthService {


    final AuthenticationManager authenticationManager;
    final UserRepo userRepo;
    final CartRepo cartRepo;
    final PasswordEncoder passwordEncoder;
    final TokenProvider tokenProvider;
    final UserDetailsService userDetailsService;

    @Override
    public LoginResponse registerUser(UserRequestCreate userRequestCreate) {

        log.info("➡ Register request received for username={}", userRequestCreate.getUsername());

        throwIf(
                //1ci hisse checker
                () -> userRepo.findUserByUsername(userRequestCreate.getUsername()).isPresent(),//optional<user1>.is
                //2ci hisse exception
                BaseException.of(USERNAME_ALREADY_REGISTERED)
        );

        log.debug("✔ Username not taken. Creating new user…");

        //mapper islemedi manual mapping etdik
        Users users = new Users();
        users.setUsername(userRequestCreate.getUsername());
        users.setPassword(passwordEncoder.encode(userRequestCreate.getPassword()));
        users.setFullName(userRequestCreate.getFullName());
        users.setIsActive(true);
        userRepo.save(users);

        log.info("✔ User created successfully: id={}, username={}", users.getId(), users.getUsername());

        //todo:  mutleq user yaradan kimi carts yarat
        Carts carts = new Carts();
        carts.setUser(users);
        cartRepo.save(carts);

        log.info("🛒 Cart created for user id={}", users.getId());

        return prepareLoginResponse(userRequestCreate.getUsername());
    }

    @Override
    public LoginResponse login(LoginRequestPayload payload) {

        log.info("➡ Login attempt for username={}", payload.getUsername());

        authenticate(payload);//bu metod pass ve useri yoxlayir

        log.info("✔ Login successful for username={}", payload.getUsername());

        return prepareLoginResponse(payload.getUsername());
    }

    @Override
    public LoginResponse refreshToken(String refreshToken) {

        log.info("➡ Refresh token request received");

        String username = tokenProvider.getUsername(refreshToken); //nuray1

        log.info("✔ Refresh token valid. Issuing new tokens for username={}", username);

        return prepareLoginResponse(username);
    }

    @Override
    public void logout() {
        log.info("➡ Logout request received. Clearing SecurityContext...");
        SecurityContextHolder.clearContext();
        log.info("✔ Logout successful");
    }

    @Override
    public void setAuthentication(String username) {

        log.debug("➡ Setting authentication for username={}", username);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
        );

        log.info("✔ Authentication set for username={}", username);
//istifadecinin kim oldugun saxlanilr
    }


//private classlar

    private LoginResponse prepareLoginResponse(String username) {

        log.debug("➡ Preparing login response for username={}", username);

        Users users = findUserByUser(username);//talib1
        String accessToken = tokenProvider.generate(users).get(0);
        String refreshToken = tokenProvider.generate(users).get(1);

        log.debug("✔ Tokens generated for username={}", username);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }

    private Users findUserByUser(String u) {
        log.debug("➡ Searching user by username={}", u);

        return userRepo.findUserByUsername(u)
                .orElseThrow(() -> {
                    log.error("❌ User not found: username={}", u);
                    return BaseException.notFound(Users.class.getSimpleName(), "user", u);
                });
    }

    private void authenticate(LoginRequestPayload request) {
        try {
            log.debug("➡ Authenticating username={}", request.getUsername());

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

        } catch (AuthenticationException e) {

            log.error("❌ Authentication failed for username={}", request.getUsername());

            throw e.getCause() instanceof BaseException ?
                    (BaseException) e.getCause() :
                    BaseException.of(INVALID_USERNAME_OR_PASSWORD);//user ve yaxud parol yanlisdir
        }
    }
}


