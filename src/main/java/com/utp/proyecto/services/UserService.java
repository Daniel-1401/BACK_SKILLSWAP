package com.utp.proyecto.services;

import com.utp.proyecto.dto.AddUserSkillRequest;
import com.utp.proyecto.dto.PagedResponse;
import com.utp.proyecto.dto.UpdateProfileRequest;
import com.utp.proyecto.dto.UserProfileResponse;
import com.utp.proyecto.dto.UserSkillMutationResponse;
import com.utp.proyecto.dto.UserSkillResponse;
import com.utp.proyecto.exceptions.ApiException;
import com.utp.proyecto.models.AppUser;
import com.utp.proyecto.models.Skill;
import com.utp.proyecto.models.UserSkill;
import com.utp.proyecto.models.UserSkillType;
import com.utp.proyecto.repositories.AppUserRepository;
import com.utp.proyecto.repositories.SkillRepository;
import com.utp.proyecto.repositories.UserSkillRepository;
import com.utp.proyecto.security.CurrentUserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class UserService {
    private final AppUserRepository userRepository;
    private final UserSkillRepository userSkillRepository;
    private final SkillRepository skillRepository;
    private final CurrentUserService currentUserService;

    public UserService(
            AppUserRepository userRepository,
            UserSkillRepository userSkillRepository,
            SkillRepository skillRepository,
            CurrentUserService currentUserService
    ) {
        this.userRepository = userRepository;
        this.userSkillRepository = userSkillRepository;
        this.skillRepository = skillRepository;
        this.currentUserService = currentUserService;
    }

    public PagedResponse<UserProfileResponse> listUsers(String q, Long categoryId, String mode, int page, int pageSize) {
        Long currentUserId = currentUserService.getCurrentUserId();
        List<AppUser> users = userRepository.findAll().stream()
                .filter(user -> !user.getId().equals(currentUserId))
                .filter(user -> matchesText(user, q))
                .filter(user -> matchesCategory(user, categoryId))
                .toList();

        int safePage = Math.max(page, 1);
        int safePageSize = Math.max(pageSize, 1);
        List<UserProfileResponse> items = users.stream()
                .skip((long) (safePage - 1) * safePageSize)
                .limit(safePageSize)
                .map(this::toProfileResponse)
                .toList();

        return new PagedResponse<>(items, safePage, safePageSize, users.size());
    }

    public UserProfileResponse getUser(Long id) {
        return toProfileResponse(findUser(id));
    }

    public UserProfileResponse getCurrentProfile() {
        return toProfileResponse(currentUserService.getCurrentUser());
    }

    public UserProfileResponse updateCurrentProfile(UpdateProfileRequest request) {
        AppUser user = currentUserService.getCurrentUser();
        if (request.name() != null) {
            user.setName(request.name());
        }
        if (request.fullName() != null) {
            user.setFullName(request.fullName());
        }
        if (request.location() != null) {
            user.setLocation(request.location());
        }
        if (request.bio() != null) {
            user.setBio(request.bio());
        }
        if (request.avatarUrl() != null) {
            user.setAvatarUrl(request.avatarUrl());
        }

        return toProfileResponse(userRepository.save(user));
    }

    public Map<String, List<UserSkillResponse>> getUserSkills(Long userId) {
        findUser(userId);
        return Map.of(
                "teaches", mapSkills(userSkillRepository.findByUserIdAndType(userId, UserSkillType.TEACHES)),
                "wants", mapSkills(userSkillRepository.findByUserIdAndType(userId, UserSkillType.WANTS))
        );
    }

    public UserSkillMutationResponse addSkillToCurrentUser(AddUserSkillRequest request, UserSkillType type) {
        if (request.skillId() == null) {
            throw ApiException.badRequest("skillId es requerido.");
        }

        Skill skill = skillRepository.findById(request.skillId())
                .orElseThrow(() -> ApiException.notFound("Habilidad no encontrada."));

        UserSkill userSkill = new UserSkill();
        userSkill.setUser(currentUserService.getCurrentUser());
        userSkill.setSkill(skill);
        userSkill.setType(type);
        userSkill.setDetail(request.detail() == null ? skill.getDescription() : request.detail());
        userSkill.setLevel(request.level() == null ? "basic" : request.level());

        return toMutationResponse(userSkillRepository.save(userSkill));
    }

    public void deleteSkillFromCurrentUser(Long userSkillId) {
        UserSkill userSkill = userSkillRepository.findById(userSkillId)
                .orElseThrow(() -> ApiException.notFound("Habilidad de usuario no encontrada."));

        if (!userSkill.getUser().getId().equals(currentUserService.getCurrentUserId())) {
            throw ApiException.forbidden("No puedes eliminar habilidades de otro usuario.");
        }

        userSkillRepository.delete(userSkill);
    }

    public UserProfileResponse toProfileResponse(AppUser user) {
        return new UserProfileResponse(
                user.getId(),
                user.getName(),
                user.getFullName(),
                user.getRating(),
                user.getExchanges(),
                user.getAvatarUrl(),
                user.getLocation(),
                user.getBio(),
                mapSkills(userSkillRepository.findByUserIdAndType(user.getId(), UserSkillType.TEACHES)),
                mapSkills(userSkillRepository.findByUserIdAndType(user.getId(), UserSkillType.WANTS))
        );
    }

    private AppUser findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Usuario no encontrado."));
    }

    private boolean matchesText(AppUser user, String q) {
        if (q == null || q.isBlank()) {
            return true;
        }
        String normalized = q.toLowerCase(Locale.ROOT);
        return user.getName().toLowerCase(Locale.ROOT).contains(normalized)
                || user.getFullName().toLowerCase(Locale.ROOT).contains(normalized)
                || matchesSkillsText(user, normalized);
    }

    private boolean matchesSkillsText(AppUser user, String normalizedQuery) {
        return userSkillRepository.findByUserId(user.getId()).stream()
                .anyMatch(userSkill -> {
                    String skillName = userSkill.getSkill().getName().toLowerCase(Locale.ROOT);
                    String detail = userSkill.getDetail() == null ? "" : userSkill.getDetail().toLowerCase(Locale.ROOT);
                    String level = userSkill.getLevel() == null ? "" : userSkill.getLevel().toLowerCase(Locale.ROOT);
                    return skillName.contains(normalizedQuery)
                            || detail.contains(normalizedQuery)
                            || level.contains(normalizedQuery);
                });
    }

    private boolean matchesCategory(AppUser user, Long categoryId) {
        if (categoryId == null) {
            return true;
        }
        return userSkillRepository.findByUserId(user.getId()).stream()
                .anyMatch(userSkill -> userSkill.getSkill().getCategory() != null
                        && categoryId.equals(userSkill.getSkill().getCategory().getId()));
    }

    private List<UserSkillResponse> mapSkills(List<UserSkill> userSkills) {
        return userSkills.stream()
                .map(userSkill -> new UserSkillResponse(
                        userSkill.getSkill().getId(),
                        userSkill.getSkill().getName(),
                        userSkill.getDetail(),
                        userSkill.getSkill().getCategory() == null ? null : userSkill.getSkill().getCategory().getId(),
                        userSkill.getLevel()
                ))
                .toList();
    }

    private UserSkillMutationResponse toMutationResponse(UserSkill userSkill) {
        return new UserSkillMutationResponse(
                userSkill.getId(),
                userSkill.getSkill().getId(),
                userSkill.getSkill().getName(),
                userSkill.getDetail(),
                userSkill.getLevel()
        );
    }
}
