package com.utp.proyecto.controllers;

import com.utp.proyecto.dto.AuthSessionResponse;
import com.utp.proyecto.dto.AuthUserResponse;
import com.utp.proyecto.dto.LoginRequest;
import com.utp.proyecto.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public AuthSessionResponse login(@RequestBody LoginRequest request) {
        return service.login(request);
    }

    @GetMapping("/me")
    public AuthUserResponse me() {
        return service.me();
    }
}
