package com.utp.proyecto.dto;

public record CreateLessonRequest(String title, String content, String videoUrl, String type, int orderIndex) {}
