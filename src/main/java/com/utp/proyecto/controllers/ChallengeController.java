package com.utp.proyecto.controllers;

import com.utp.proyecto.dto.ApplicationResponse;
import com.utp.proyecto.dto.ApplyRequest;
import com.utp.proyecto.dto.ChallengeResponse;
import com.utp.proyecto.dto.CompanyProfileResponse;
import com.utp.proyecto.dto.CreateChallengeRequest;
import com.utp.proyecto.dto.CreateCompanyProfileRequest;
import com.utp.proyecto.services.ChallengeService;
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
@RequestMapping("/api/v1/challenges")
@Tag(name = "Challenges", description = "Desafíos empresariales — Pilar 2")
public class ChallengeController {

    private final ChallengeService challengeService;

    public ChallengeController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    @PostMapping("/company")
    @Operation(summary = "Registrar perfil de empresa")
    public ResponseEntity<CompanyProfileResponse> registerCompany(@RequestBody CreateCompanyProfileRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(challengeService.registerCompany(request));
    }

    @GetMapping("/company/me")
    @Operation(summary = "Ver mi perfil de empresa")
    public CompanyProfileResponse getMyCompany() {
        return challengeService.getMyCompanyProfile();
    }

    @GetMapping
    @Operation(summary = "Listar desafíos abiertos")
    public List<ChallengeResponse> listChallenges() {
        return challengeService.listOpenChallenges();
    }

    @GetMapping("/me")
    @Operation(summary = "Mis desafíos publicados como empresa")
    public List<ChallengeResponse> myChallenges() {
        return challengeService.listMyChallenges();
    }

    @PostMapping
    @Operation(summary = "Publicar un desafío")
    public ResponseEntity<ChallengeResponse> createChallenge(@RequestBody CreateChallengeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(challengeService.createChallenge(request));
    }

    @PostMapping("/{id}/apply")
    @Operation(summary = "Postularse a un desafío")
    public ChallengeResponse apply(@PathVariable Long id, @RequestBody ApplyRequest request) {
        return challengeService.applyToChallenge(id, request);
    }

    @GetMapping("/{id}/applications")
    @Operation(summary = "Ver postulaciones al desafío (empresa dueña)")
    public List<ApplicationResponse> getApplications(@PathVariable Long id) {
        return challengeService.getChallengeApplications(id);
    }

    @PostMapping("/{id}/select/{applicationId}")
    @Operation(summary = "Seleccionar ganador del desafío")
    public ChallengeResponse selectWinner(@PathVariable Long id, @PathVariable Long applicationId) {
        return challengeService.selectWinner(id, applicationId);
    }
}
