package com.utp.proyecto.dto;

public record MessageResponse(
        Long id,
        Long conversationId,
        Long senderId,
        String senderName,
        String body,
        String createdAt
) {
}
