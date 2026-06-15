package com.utp.proyecto.dto;

public record ExchangeResponse(
        Long id,
        String type,
        String personName,
        String avatarUrl,
        String timeLabel,
        String status,
        String statusLabel,
        String wantsToLearn,
        String offersToTeach,
        String message
) {
}
