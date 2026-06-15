package com.utp.proyecto.dto;

public record UpdateProfileRequest(String name, String fullName, String location, String bio, String avatarUrl) {
}
