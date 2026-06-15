package com.utp.proyecto.dto;

public record SessionResponse(
        Long id,
        String mode,
        String status,
        String title,
        String personRole,
        String personName,
        String month,
        String day,
        String date,
        String time,
        String duration
) {
}
