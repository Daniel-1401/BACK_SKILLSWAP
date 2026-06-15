package com.utp.proyecto.services;

import com.utp.proyecto.dto.AuthSessionResponse;
import com.utp.proyecto.dto.AuthUserResponse;
import com.utp.proyecto.dto.LoginRequest;
import com.utp.proyecto.exceptions.ApiException;
import com.utp.proyecto.models.AppUser;
import com.utp.proyecto.repositories.AppUserRepository;
import com.utp.proyecto.security.CurrentUserService;
import com.utp.proyecto.security.JwtService;
import com.utp.proyecto.security.PasswordHasher;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AppUserRepository userRepository;
    private final CurrentUserService currentUserService;
    private final PasswordHasher passwordHasher;
    private final JwtService jwtService;

    public AuthService(
            AppUserRepository userRepository,
            CurrentUserService currentUserService,
            PasswordHasher passwordHasher,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
        this.passwordHasher = passwordHasher;
        this.jwtService = jwtService;
    }

    public AuthSessionResponse login(LoginRequest request) {
        if (request.email() == null || request.password() == null) {
            throw ApiException.badRequest("Email y password son requeridos.");
        }

        AppUser user = userRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> ApiException.unauthorized("Correo o contrasena incorrectos."));

        if (!passwordHasher.matches(request.password(), user.getPasswordHash())) {
            throw ApiException.unauthorized("Correo o contrasena incorrectos.");
        }

        String token = jwtService.generateToken(user.getId(), user.getEmail());
        return new AuthSessionResponse(token, toAuthUser(user));
    }

    public AuthUserResponse me() {
        return toAuthUser(currentUserService.getCurrentUser());
    }

    private AuthUserResponse toAuthUser(AppUser user) {
        return new AuthUserResponse(user.getId(), user.getName(), user.getEmail(), user.getCredits());
    }
}
