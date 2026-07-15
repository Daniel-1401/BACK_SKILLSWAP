package com.utp.proyecto.dto;

import java.time.LocalDateTime;

public record CourseSummaryResponse(
    Long id,
    String title,
    String description,
    int priceCredits,
    String coverUrl,
    String level,
    String status,
    boolean isCertifiable,
    Long instructorId,
    String instructorName,
    String instructorAvatar,
    Long categoryId,
    String categoryName,
    long lessonsCount,
    long enrollmentsCount,
    LocalDateTime createdAt
) {}
