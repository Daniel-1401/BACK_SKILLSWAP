package com.utp.proyecto.controllers;

import com.utp.proyecto.dto.CreateExchangeRequest;
import com.utp.proyecto.dto.CreateExchangeResponse;
import com.utp.proyecto.dto.ExchangeActionResponse;
import com.utp.proyecto.dto.ExchangeResponse;
import com.utp.proyecto.services.ExchangeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exchanges")
public class ExchangeController {
    private final ExchangeService service;

    public ExchangeController(ExchangeService service) {
        this.service = service;
    }

    @GetMapping
    public List<ExchangeResponse> list(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status
    ) {
        return service.list(type, status);
    }

    @PostMapping
    public ResponseEntity<CreateExchangeResponse> create(@RequestBody CreateExchangeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PostMapping("/{id}/accept")
    public ExchangeActionResponse accept(@PathVariable Long id) {
        return service.accept(id);
    }

    @PostMapping("/{id}/reject")
    public ExchangeActionResponse reject(@PathVariable Long id) {
        return service.reject(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        service.cancel(id);
        return ResponseEntity.noContent().build();
    }
}
