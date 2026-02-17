package org.example.demo13213.constant;


public final class SecurityPathConstants {

    //sadece test ucun yaradilib
    public static final String[] PERMIT_ALL = {
            "/swagger-ui.html",
            "/webjars/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/api/v1/auth/register-user",
            "/api/v1/auth/login",
            "/api/v1/auth/no-auth",
            "/api/v1/auth/refresh-token/**",
            "/api/v1/products/search/**",
            "/api/v1/products/bestsellers",
            "/api/v1/catalog/brands-with-products/**",
            "/api/v1/catalog/home",
            "/api/v1/products/filter-products",
    };
    public static final String[] AUTHENTICATED = {
            "/api/v1/auth/logout/**",
            "/api/v1/cart/**",
            "/api/v1/orders/**",
            "/api/v1/reviews/**",
            "/api/admin/stats/**"
    };


}
