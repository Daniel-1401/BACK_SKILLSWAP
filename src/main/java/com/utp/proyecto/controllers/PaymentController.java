package com.utp.proyecto.controllers;

import com.utp.proyecto.dto.PaymentCheckoutRequest;
import com.utp.proyecto.dto.PaymentCheckoutResponse;
import com.utp.proyecto.dto.PaymentConfirmRequest;
import com.utp.proyecto.dto.PaymentConfirmResponse;
import com.utp.proyecto.dto.PaymentHistoryResponse;
import com.utp.proyecto.services.BillingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final BillingService service;

    public PaymentController(BillingService service) {
        this.service = service;
    }

    @PostMapping("/checkout")
    public ResponseEntity<PaymentCheckoutResponse> checkout(@RequestBody PaymentCheckoutRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.checkout(request));
    }

    @PostMapping("/confirm")
    public PaymentConfirmResponse confirm(@RequestBody PaymentConfirmRequest request) {
        return service.confirm(request);
    }

    @GetMapping("/history")
    public List<PaymentHistoryResponse> history() {
        return service.getPaymentHistory();
    }
}
