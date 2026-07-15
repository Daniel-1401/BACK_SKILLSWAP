package com.utp.proyecto.dto;

import java.time.LocalDateTime;

public record ChallengeResponse(
    Long id,
    String title,
    String description,
    int creditsReward,
    String status,
    LocalDateTime deadline,
    LocalDateTime createdAt,
    Long companyId,
    String companyName,
    Long categoryId,
    String categoryName,
    long applicationsCount,
    boolean applied
) {}
