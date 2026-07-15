package com.utp.proyecto.dto;

import java.time.LocalDateTime;

public record CreateHelpRequestRequest(String title, String description, Long categoryId, int creditsOffered, LocalDateTime deadline) {}
