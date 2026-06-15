package com.utp.proyecto.dto;

public record CreateExchangeRequest(Long targetUserId, Long skillWantedId, Long skillOfferedId, String message) {
}
