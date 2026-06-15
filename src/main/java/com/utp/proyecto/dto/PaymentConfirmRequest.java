package com.utp.proyecto.dto;

public record PaymentConfirmRequest(Long paymentId, String providerTransactionId) {
}
