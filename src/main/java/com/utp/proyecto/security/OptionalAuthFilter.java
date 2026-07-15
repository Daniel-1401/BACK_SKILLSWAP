package com.utp.proyecto.security;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Runs for every request (including public endpoints excluded from AuthInterceptor).
 * If a valid JWT is present, extracts and sets the user ID attribute silently.
 * Never throws — authentication enforcement is left to AuthInterceptor.
 */
@Component
@Order(1)
public class OptionalAuthFilter implements Filter {

    private final JwtService jwtService;

    public OptionalAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        String auth = request.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            try {
                Long userId = jwtService.validateAndGetUserId(auth.substring(7));
                request.setAttribute(AuthInterceptor.CURRENT_USER_ID, userId);
            } catch (Exception ignored) {
                // Invalid token on a public endpoint — just proceed as anonymous
            }
        }
        chain.doFilter(req, res);
    }
}
