package com.utp.proyecto.dto;

public record ReviewResponse(Long id, Long sessionId, String reviewerName, int rating, String comment, String createdAt) {
}
