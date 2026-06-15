package com.utp.proyecto.dto;

import java.math.BigDecimal;

public record PaymentHistoryResponse(
        Long paymentId,
        String planId,
        String status,
        BigDecimal amount,
        String currency,
        Integer creditsAdded,
        String createdAt
) {
}
