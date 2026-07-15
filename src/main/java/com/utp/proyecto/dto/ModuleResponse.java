package com.utp.proyecto.dto;

import java.util.List;

public record ModuleResponse(Long id, String title, int orderIndex, List<LessonResponse> lessons) {}
