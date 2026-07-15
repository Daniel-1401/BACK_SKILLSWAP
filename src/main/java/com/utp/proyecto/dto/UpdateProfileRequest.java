package com.utp.proyecto.dto;

public record UpdateProfileRequest(
        String name,
        String fullName,
        String email,
        String location,
        String bio,
        String avatarUrl,
        String currentPassword,
        String newPassword
) {
}
