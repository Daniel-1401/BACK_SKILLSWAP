package com.utp.proyecto.dto;

public record CreditHistoryResponse(Long id, int amount, int balanceAfter, String description, String createdAt) {
}
