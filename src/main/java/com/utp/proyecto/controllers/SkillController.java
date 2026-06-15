package com.utp.proyecto.controllers;

import com.utp.proyecto.dto.PagedResponse;
import com.utp.proyecto.dto.SkillFeaturedResponse;
import com.utp.proyecto.dto.SkillSearchResponse;
import com.utp.proyecto.services.SkillService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/skills")
public class SkillController {

    private final SkillService service;

    public SkillController(SkillService service) {
        this.service = service;
    }

    @GetMapping("/featured")
    public List<SkillFeaturedResponse> getFeatured() {
        return service.getFeaturedSkills();
    }

    @GetMapping
    public PagedResponse<SkillSearchResponse> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return service.search(q, categoryId, page, pageSize);
    }
}
