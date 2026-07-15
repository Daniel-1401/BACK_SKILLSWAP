package com.utp.proyecto.dto;

public record CourseProgressResponse(long totalLessons, long completedLessons, int percentage, boolean completed) {}
