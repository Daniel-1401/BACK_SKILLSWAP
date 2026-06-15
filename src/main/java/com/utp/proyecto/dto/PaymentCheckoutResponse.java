package com.utp.proyecto.dto;

import java.math.BigDecimal;

public record PaymentCheckoutResponse(Long paymentId, String status, BigDecimal amount, String currency) {
}
