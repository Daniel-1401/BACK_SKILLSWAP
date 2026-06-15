package com.utp.proyecto.services;

import com.utp.proyecto.dto.CategoryResponse;
import com.utp.proyecto.dto.ModalityResponse;
import com.utp.proyecto.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogService {
    private final CategoryRepository categoryRepository;

    public CatalogService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryResponse> getCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> new CategoryResponse(category.getId(), category.getName()))
                .toList();
    }

    public List<ModalityResponse> getModalities() {
        return List.of(
                new ModalityResponse("online", "Online (Videollamada)"),
                new ModalityResponse("presential", "Presencial (En campus)")
        );
    }
}
