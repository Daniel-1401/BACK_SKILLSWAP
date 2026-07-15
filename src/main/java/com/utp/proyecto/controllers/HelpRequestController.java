package com.utp.proyecto.controllers;

import com.utp.proyecto.dto.ApplicationResponse;
import com.utp.proyecto.dto.ApplyRequest;
import com.utp.proyecto.dto.CreateHelpRequestRequest;
import com.utp.proyecto.dto.HelpRequestResponse;
import com.utp.proyecto.services.HelpRequestService;
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
@RequestMapping("/api/v1/help-requests")
@Tag(name = "Help Requests", description = "Solicitudes de ayuda entre usuarios — Pilar 4")
public class HelpRequestController {

    private final HelpRequestService helpRequestService;

    public HelpRequestController(HelpRequestService helpRequestService) {
        this.helpRequestService = helpRequestService;
    }

    @GetMapping
    @Operation(summary = "Listar solicitudes de ayuda abiertas")
    public List<HelpRequestResponse> listRequests() {
        return helpRequestService.listOpenRequests();
    }

    @GetMapping("/me")
    @Operation(summary = "Mis solicitudes publicadas")
    public List<HelpRequestResponse> myRequests() {
        return helpRequestService.getMyRequests();
    }

    @PostMapping
    @Operation(summary = "Publicar solicitud de ayuda (descuenta créditos del poster)")
    public ResponseEntity<HelpRequestResponse> createRequest(@RequestBody CreateHelpRequestRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(helpRequestService.createRequest(request));
    }

    @PostMapping("/{id}/apply")
    @Operation(summary = "Postularse a resolver una solicitud")
    public HelpRequestResponse apply(@PathVariable Long id, @RequestBody ApplyRequest request) {
        return helpRequestService.applyToRequest(id, request);
    }

    @GetMapping("/{id}/applications")
    @Operation(summary = "Ver postulantes a mi solicitud")
    public List<ApplicationResponse> getApplications(@PathVariable Long id) {
        return helpRequestService.getRequestApplications(id);
    }

    @PostMapping("/{id}/select/{applicationId}")
    @Operation(summary = "Seleccionar ayudante (transfiere créditos)")
    public HelpRequestResponse selectHelper(@PathVariable Long id, @PathVariable Long applicationId) {
        return helpRequestService.selectHelper(id, applicationId);
    }
}
