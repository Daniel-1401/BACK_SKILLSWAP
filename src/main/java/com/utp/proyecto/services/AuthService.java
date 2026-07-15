package com.utp.proyecto.services;

import com.utp.proyecto.dto.AuthSessionResponse;
import com.utp.proyecto.dto.AuthUserResponse;
import com.utp.proyecto.dto.LoginRequest;
import com.utp.proyecto.dto.RegisterRequest;
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

    public AuthSessionResponse register(RegisterRequest request) {
        if (request.name() == null || request.name().isBlank()
                || request.fullName() == null || request.fullName().isBlank()
                || request.email() == null || request.email().isBlank()
                || request.password() == null || request.password().isBlank()) {
            throw ApiException.badRequest("Nombre, nombre completo, email y password son requeridos.");
        }

        userRepository.findByEmailIgnoreCase(request.email()).ifPresent(user -> {
            throw ApiException.badRequest("Ya existe un usuario con ese email.");
        });

        AppUser user = new AppUser();
        user.setName(request.name().trim());
        user.setFullName(request.fullName().trim());
        user.setEmail(request.email().trim().toLowerCase());
        user.setPasswordHash(passwordHasher.hash(request.password()));
        user.setCredits(1);
        user.setRating(0.0);
        user.setExchanges(0);
        user.setAvatarUrl(defaultAvatarUrl(request.name(), request.fullName(), request.avatarUrl()));
        user.setLocation(request.location() == null || request.location().isBlank() ? "Lima, Peru" : request.location().trim());
        user.setBio(request.bio() == null || request.bio().isBlank()
                ? "Nuevo usuario en SkillSwap."
                : request.bio().trim());

        AppUser savedUser = userRepository.save(user);
        String token = jwtService.generateToken(savedUser.getId(), savedUser.getEmail());
        return new AuthSessionResponse(token, toAuthUser(savedUser));
    }

    public AuthUserResponse me() {
        return toAuthUser(currentUserService.getCurrentUser());
    }

    private AuthUserResponse toAuthUser(AppUser user) {
        return new AuthUserResponse(user.getId(), user.getName(), user.getEmail(), user.getCredits(), user.getAvatarUrl());
    }

    private String defaultAvatarUrl(String name, String fullName, String avatarUrl) {
        if (avatarUrl != null && !avatarUrl.isBlank()) {
            return avatarUrl.trim();
        }

        String displayName = fullName != null && !fullName.isBlank() ? fullName.trim() : name.trim();
        String encodedName = displayName.replace(" ", "+");
        return "https://ui-avatars.com/api/?name=" + encodedName + "&background=0D6EFD&color=fff";
    }
}
