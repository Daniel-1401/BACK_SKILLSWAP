package com.utp.proyecto.dto;

import java.time.LocalDateTime;

public record HelpRequestResponse(
    Long id,
    String title,
    String description,
    int creditsOffered,
    String status,
    LocalDateTime deadline,
    LocalDateTime createdAt,
    Long posterId,
    String posterName,
    String posterAvatar,
    Long categoryId,
    String categoryName,
    long applicationsCount,
    boolean applied
) {}
