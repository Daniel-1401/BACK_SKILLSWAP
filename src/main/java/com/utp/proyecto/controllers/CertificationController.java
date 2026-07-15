package com.utp.proyecto.controllers;

import com.utp.proyecto.dto.CertificationResponse;
import com.utp.proyecto.dto.IssueCertificationRequest;
import com.utp.proyecto.services.CertificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/certifications")
@Tag(name = "Certifications", description = "Certificaciones verificables — Pilar 3")
public class CertificationController {

    private final CertificationService certificationService;

    public CertificationController(CertificationService certificationService) {
        this.certificationService = certificationService;
    }

    @PostMapping
    @Operation(summary = "Emitir certificación al completar un curso certificable")
    public ResponseEntity<CertificationResponse> issue(@RequestBody IssueCertificationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(certificationService.issueCertification(request));
    }

    @GetMapping("/me")
    @Operation(summary = "Mis certificaciones")
    public List<CertificationResponse> myCertifications() {
        return certificationService.getMyCertifications();
    }

    @GetMapping("/verify/{code}")
    @Operation(summary = "Verificar certificado por código (público)")
    public CertificationResponse verify(@PathVariable String code) {
        return certificationService.verifyByCode(code);
    }
}
