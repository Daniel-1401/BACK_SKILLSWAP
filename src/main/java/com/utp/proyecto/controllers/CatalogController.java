package com.utp.proyecto.controllers;

import com.utp.proyecto.dto.CategoryResponse;
import com.utp.proyecto.dto.ModalityResponse;
import com.utp.proyecto.services.CatalogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalog")
public class CatalogController {
    private final CatalogService service;

    public CatalogController(CatalogService service) {
        this.service = service;
    }

    @GetMapping("/categories")
    public List<CategoryResponse> getCategories() {
        return service.getCategories();
    }

    @GetMapping("/modalities")
    public List<ModalityResponse> getModalities() {
        return service.getModalities();
    }
}
