package com.utp.proyecto.dto;

public record CreateExchangeResponse(
        Long id,
        String type,
        String status,
        Long targetUserId,
        Long skillWantedId,
        Long skillOfferedId,
        String message
) {
}
