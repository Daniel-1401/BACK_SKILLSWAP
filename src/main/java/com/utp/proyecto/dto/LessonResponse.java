package com.utp.proyecto.dto;

public record LessonResponse(Long id, String title, String content, String videoUrl, String type, int orderIndex, boolean completed) {}
