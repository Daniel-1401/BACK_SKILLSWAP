package com.utp.proyecto.dto;

public record RegisterRequest(
        String name,
        String fullName,
        String email,
        String password,
        String location,
        String bio,
        String avatarUrl
) {
}
