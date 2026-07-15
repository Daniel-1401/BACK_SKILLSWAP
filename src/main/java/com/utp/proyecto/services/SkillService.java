package com.utp.proyecto.services;

import com.utp.proyecto.dto.PagedResponse;
import com.utp.proyecto.dto.SkillFeaturedResponse;
import com.utp.proyecto.dto.SkillSearchResponse;
import com.utp.proyecto.models.AppUser;
import com.utp.proyecto.models.Skill;
import com.utp.proyecto.models.UserSkill;
import com.utp.proyecto.models.UserSkillType;
import com.utp.proyecto.repositories.AppUserRepository;
import com.utp.proyecto.repositories.SkillRepository;
import com.utp.proyecto.repositories.UserSkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

@Service
public class SkillService {

    private final SkillRepository repository;
    private final AppUserRepository userRepository;
    private final UserSkillRepository userSkillRepository;

    public SkillService(SkillRepository repository, AppUserRepository userRepository, UserSkillRepository userSkillRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.userSkillRepository = userSkillRepository;
    }

    public List<SkillFeaturedResponse> getFeaturedSkills() {
        List<SkillFeaturedResponse> catalogSkills = repository.findAll().stream()
                .map(this::toFeaturedResponse)
                .toList();

        List<SkillFeaturedResponse> userTaughtSkills = userSkillRepository.findByType(UserSkillType.TEACHES).stream()
                .filter(userSkill -> !isAlreadyFeatured(userSkill))
                .map(this::toFeaturedResponseFromUserSkill)
                .toList();

        return Stream.concat(catalogSkills.stream(), userTaughtSkills.stream()).toList();
    }

    private boolean isAlreadyFeatured(UserSkill userSkill) {
        Long tutorId = resolveTutorId(userSkill.getSkill().getTutor());
        return tutorId != null && tutorId.equals(userSkill.getUser().getId());
    }

    private SkillFeaturedResponse toFeaturedResponseFromUserSkill(UserSkill userSkill) {
        Skill skill = userSkill.getSkill();
        AppUser tutor = userSkill.getUser();
        String description = userSkill.getDetail() != null && !userSkill.getDetail().isBlank()
                ? userSkill.getDetail()
                : skill.getDescription();
        String imageUrl = userSkill.getImageUrl() != null && !userSkill.getImageUrl().isBlank()
                ? userSkill.getImageUrl()
                : null;

        return new SkillFeaturedResponse(
                -userSkill.getId(),
                skill.getName(),
                tutor.getId(),
                tutor.getName(),
                description,
                skill.getSubscriptionCost(),
                imageUrl
        );
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
        Long tutorId = resolveTutorId(skill.getTutor());
        return new SkillFeaturedResponse(
                skill.getId(),
                skill.getName(),
                tutorId,
                skill.getTutor(),
                skill.getDescription(),
                skill.getSubscriptionCost(),
                resolveImageUrl(skill, tutorId)
        );
    }

    private Long resolveTutorId(String tutorName) {
        if (tutorName == null || tutorName.isBlank()) {
            return null;
        }

        return userRepository.findFirstByNameIgnoreCaseOrFullNameIgnoreCase(tutorName, tutorName)
                .map(AppUser::getId)
                .orElse(null);
    }

    private String resolveImageUrl(Skill skill, Long tutorId) {
        if (tutorId == null) {
            return null;
        }

        return userSkillRepository.findByUserIdAndType(tutorId, UserSkillType.TEACHES).stream()
                .filter(userSkill -> userSkill.getSkill().getId().equals(skill.getId()))
                .map(userSkill -> userSkill.getImageUrl())
                .filter(imageUrl -> imageUrl != null && !imageUrl.isBlank())
                .findFirst()
                .orElse(null);
    }

    private SkillSearchResponse toSearchResponse(Skill skill) {
        Long categoryId = skill.getCategory() == null ? null : skill.getCategory().getId();
        return new SkillSearchResponse(skill.getId(), skill.getName(), skill.getDescription(), categoryId);
    }
}
