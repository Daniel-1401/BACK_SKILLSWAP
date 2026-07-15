package com.utp.proyecto.dto;

public record CreateCourseRequest(
    String title,
    String description,
    Long categoryId,
    int priceCredits,
    String coverUrl,
    String level
) {}
