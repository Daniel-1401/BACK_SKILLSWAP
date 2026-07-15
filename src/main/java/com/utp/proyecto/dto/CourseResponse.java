package com.utp.proyecto.dto;

import java.time.LocalDateTime;
import java.util.List;

public record CourseResponse(
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
    long modulesCount,
    long lessonsCount,
    long enrollmentsCount,
    LocalDateTime createdAt,
    boolean enrolled,
    int progressPercentage,
    List<ModuleResponse> modules
) {}
