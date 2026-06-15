package com.utp.proyecto.services;

import com.utp.proyecto.dto.PagedResponse;
import com.utp.proyecto.dto.SkillFeaturedResponse;
import com.utp.proyecto.dto.SkillSearchResponse;
import com.utp.proyecto.models.Skill;
import com.utp.proyecto.repositories.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class SkillService {

    private final SkillRepository repository;

    public SkillService(SkillRepository repository) {
        this.repository = repository;
    }

    public List<SkillFeaturedResponse> getFeaturedSkills() {
        return repository.findAll().stream()
                .map(this::toFeaturedResponse)
                .toList();
    }

    public PagedResponse<SkillSearchResponse> search(String q, Long categoryId, int page, int pageSize) {
        List<Skill> skills = repository.findAll().stream()
                .filter(skill -> q == null || q.isBlank() || skill.getName().toLowerCase(Locale.ROOT).contains(q.toLowerCase(Locale.ROOT)))
                .filter(skill -> categoryId == null || (skill.getCategory() != null && categoryId.equals(skill.getCategory().getId())))
                .toList();

        int safePage = Math.max(page, 1);
        int safePageSize = Math.max(pageSize, 1);
        List<SkillSearchResponse> items = skills.stream()
                .skip((long) (safePage - 1) * safePageSize)
                .limit(safePageSize)
                .map(this::toSearchResponse)
                .toList();

        return new PagedResponse<>(items, safePage, safePageSize, skills.size());
    }

    private SkillFeaturedResponse toFeaturedResponse(Skill skill) {
        return new SkillFeaturedResponse(
                skill.getId(),
                skill.getName(),
                skill.getTutor(),
                skill.getDescription(),
                skill.getSubscriptionCost()
        );
    }

    private SkillSearchResponse toSearchResponse(Skill skill) {
        Long categoryId = skill.getCategory() == null ? null : skill.getCategory().getId();
        return new SkillSearchResponse(skill.getId(), skill.getName(), skill.getDescription(), categoryId);
    }
}
