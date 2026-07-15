package com.utp.proyecto.dto;

import java.time.LocalDateTime;

public record ApplicationResponse(Long id, Long userId, String userName, String userAvatar, String proposal, LocalDateTime appliedAt, boolean selected) {}
