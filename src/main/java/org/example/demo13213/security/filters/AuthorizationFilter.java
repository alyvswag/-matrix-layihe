package org.example.demo13213.security.filters;



import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.demo13213.security.jwt.TokenProvider;
import org.example.demo13213.service.auth.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.example.demo13213.constant.TokenConstants.PREFIX;


@Component
@RequiredArgsConstructor
@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter {

    final TokenProvider tokenProvider;
    final UserDetailsService userDetailsService;
    final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();

        // Yalnız açıq olan endpoint-ləri bypass et
        if (path.equals("/api/v1/auth/register-user") || path.equals("/api/v1/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Authorization header yoxlanışı
        String tokenRequest = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (tokenRequest != null && tokenRequest.startsWith(PREFIX)) {
            String token = tokenRequest.substring(PREFIX.length());
            authService.setAuthentication(tokenProvider.getEmail(token));
        }

        filterChain.doFilter(request, response);
    }

}
