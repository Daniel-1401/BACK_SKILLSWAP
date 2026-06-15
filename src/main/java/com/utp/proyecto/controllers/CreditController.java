package com.utp.proyecto.controllers;

import com.utp.proyecto.dto.CreditBalanceResponse;
import com.utp.proyecto.dto.CreditHistoryResponse;
import com.utp.proyecto.services.BillingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/credits")
public class CreditController {
    private final BillingService service;

    public CreditController(BillingService service) {
        this.service = service;
    }

    @GetMapping("/balance")
    public CreditBalanceResponse getBalance() {
        return service.getBalance();
    }

    @GetMapping("/history")
    public List<CreditHistoryResponse> getHistory() {
        return service.getCreditHistory();
    }
}
