package com.utp.proyecto.dto;

public record ConversationResponse(
        Long id,
        String participantName,
        String participantAvatarUrl,
        String lastMessage,
        long unreadCount,
        String updatedAt
) {
}
