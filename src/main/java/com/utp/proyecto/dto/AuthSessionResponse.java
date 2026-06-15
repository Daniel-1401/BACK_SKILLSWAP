package com.utp.proyecto.dto;

public record AuthSessionResponse(String accessToken, AuthUserResponse user) {
}
