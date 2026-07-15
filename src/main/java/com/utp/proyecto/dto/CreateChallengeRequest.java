package com.utp.proyecto.dto;

import java.time.LocalDateTime;

public record CreateChallengeRequest(String title, String description, int creditsReward, Long categoryId, LocalDateTime deadline) {}
