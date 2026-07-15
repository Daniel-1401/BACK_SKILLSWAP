package com.utp.proyecto.dto;

import java.time.LocalDateTime;

public record CertificationResponse(
    Long id,
    Long userId,
    String userName,
    Long courseId,
    String courseName,
    String fullName,
    String dni,
    LocalDateTime issuedAt,
    String certificateCode
) {}
