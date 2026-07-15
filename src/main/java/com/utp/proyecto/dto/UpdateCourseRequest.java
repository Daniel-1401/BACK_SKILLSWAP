package com.utp.proyecto.dto;

public record UpdateCourseRequest(
    String title,
    String description,
    Long categoryId,
    int priceCredits,
    String coverUrl,
    String level,
    String status,
    Boolean isCertifiable
) {}
