package com.utp.proyecto.security;

import com.utp.proyecto.exceptions.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    public static final String CURRENT_USER_ID = "currentUserId";

    private final JwtService jwtService;

    public AuthInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw ApiException.unauthorized("Token requerido.");
        }

        Long userId = jwtService.validateAndGetUserId(authorization.substring("Bearer ".length()));
        request.setAttribute(CURRENT_USER_ID, userId);
        return true;
    }
}
