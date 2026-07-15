package com.utp.proyecto.security;

import com.utp.proyecto.exceptions.ApiException;
import com.utp.proyecto.models.AppUser;
import com.utp.proyecto.repositories.AppUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {
    private final HttpServletRequest request;
    private final AppUserRepository userRepository;

    public CurrentUserService(HttpServletRequest request, AppUserRepository userRepository) {
        this.request = request;
        this.userRepository = userRepository;
    }

    public Long getCurrentUserId() {
        Object value = request.getAttribute(AuthInterceptor.CURRENT_USER_ID);
        if (value instanceof Long userId) {
            return userId;
        }
        throw ApiException.unauthorized("Usuario no autenticado.");
    }

    public Long getCurrentUserIdOrNull() {
        Object value = request.getAttribute(AuthInterceptor.CURRENT_USER_ID);
        return value instanceof Long userId ? userId : null;
    }

    public AppUser getCurrentUser() {
        return userRepository.findById(getCurrentUserId())
                .orElseThrow(() -> ApiException.unauthorized("Usuario no autenticado."));
    }
}
