package com.kaliv.myths.constant;

public class SecurityConstants {
    public static final long EXPIRATION_TIME = 432_000_000; // 5 days expressed in milliseconds
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
    public static final String PANDA_SOFT_LLC = "Panda Soft, LLC";
    public static final String PANDA_SOFT_ADMINISTRATION = "User Management Portal";
    public static final String AUTHORITIES = "authorities";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
    public static final String[] PUBLIC_URLS = {"/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**",
            "/api/v1/users/login", "/api/v1/users/register"};
}
