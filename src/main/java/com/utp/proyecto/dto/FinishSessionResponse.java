package com.utp.proyecto.dto;

public record FinishSessionResponse(Long sessionId, String status, int creditsAdded, int newBalance) {
}
