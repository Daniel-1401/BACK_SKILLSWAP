package com.utp.proyecto.dto;

import java.util.List;

public record UserProfileResponse(
        Long id,
        String name,
        String fullName,
        String email,
        double rating,
        int exchanges,
        String avatarUrl,
        String location,
        String bio,
        List<UserSkillResponse> teaches,
        List<UserSkillResponse> wants
) {
}
