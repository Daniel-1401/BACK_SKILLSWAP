package com.utp.proyecto.controllers;

import com.utp.proyecto.dto.PlanResponse;
import com.utp.proyecto.services.BillingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/plans")
public class PlanController {
    private final BillingService service;

    public PlanController(BillingService service) {
        this.service = service;
    }

    @GetMapping
    public List<PlanResponse> getPlans() {
        return service.getPlans();
    }
}
