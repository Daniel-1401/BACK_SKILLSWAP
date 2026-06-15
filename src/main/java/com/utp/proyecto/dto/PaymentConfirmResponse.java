package com.utp.proyecto.dto;

public record PaymentConfirmResponse(Long paymentId, String status, int creditsAdded, int newBalance) {
}
