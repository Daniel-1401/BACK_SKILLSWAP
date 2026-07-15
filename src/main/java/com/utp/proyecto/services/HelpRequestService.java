package com.utp.proyecto.services;

import com.utp.proyecto.dto.ApplicationResponse;
import com.utp.proyecto.dto.ApplyRequest;
import com.utp.proyecto.dto.CreateHelpRequestRequest;
import com.utp.proyecto.dto.HelpRequestResponse;
import com.utp.proyecto.exceptions.ApiException;
import com.utp.proyecto.models.AppUser;
import com.utp.proyecto.models.HelpApplication;
import com.utp.proyecto.models.HelpRequest;
import com.utp.proyecto.models.HelpRequestStatus;
import com.utp.proyecto.repositories.AppUserRepository;
import com.utp.proyecto.repositories.CategoryRepository;
import com.utp.proyecto.repositories.HelpApplicationRepository;
import com.utp.proyecto.repositories.HelpRequestRepository;
import com.utp.proyecto.security.CurrentUserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HelpRequestService {

    private final HelpRequestRepository helpRequestRepository;
    private final HelpApplicationRepository applicationRepository;
    private final AppUserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CurrentUserService currentUserService;

    public HelpRequestService(HelpRequestRepository helpRequestRepository,
                              HelpApplicationRepository applicationRepository,
                              AppUserRepository userRepository,
                              CategoryRepository categoryRepository,
                              CurrentUserService currentUserService) {
        this.helpRequestRepository = helpRequestRepository;
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.currentUserService = currentUserService;
    }

    public List<HelpRequestResponse> listOpenRequests() {
        Long userId = safeCurrentUserId();
        return helpRequestRepository.findByStatus(HelpRequestStatus.OPEN).stream()
                .map(r -> toResponse(r, userId))
                .toList();
    }

    public List<HelpRequestResponse> getMyRequests() {
        Long userId = currentUserService.getCurrentUserId();
        return helpRequestRepository.findByPosterId(userId).stream()
                .map(r -> toResponse(r, userId))
                .toList();
    }

    public HelpRequestResponse createRequest(CreateHelpRequestRequest request) {
        Long userId = currentUserService.getCurrentUserId();
        AppUser poster = findUser(userId);
        if (poster.getCredits() < request.creditsOffered()) {
            throw ApiException.badRequest("Créditos insuficientes para publicar la solicitud.");
        }
        poster.setCredits(poster.getCredits() - request.creditsOffered());
        userRepository.save(poster);

        HelpRequest hr = new HelpRequest();
        hr.setPoster(poster);
        hr.setTitle(request.title());
        hr.setDescription(request.description());
        hr.setCreditsOffered(request.creditsOffered());
        hr.setStatus(HelpRequestStatus.OPEN);
        hr.setDeadline(request.deadline());
        hr.setCreatedAt(LocalDateTime.now());
        if (request.categoryId() != null) {
            hr.setCategory(categoryRepository.findById(request.categoryId()).orElse(null));
        }
        return toResponse(helpRequestRepository.save(hr), userId);
    }

    public HelpRequestResponse applyToRequest(Long requestId, ApplyRequest request) {
        Long userId = currentUserService.getCurrentUserId();
        HelpRequest hr = findRequest(requestId);
        if (!hr.getStatus().equals(HelpRequestStatus.OPEN)) {
            throw ApiException.badRequest("Esta solicitud ya no está abierta.");
        }
        if (applicationRepository.findByHelpRequestIdAndUserId(requestId, userId).isPresent()) {
            throw ApiException.badRequest("Ya aplicaste a esta solicitud.");
        }
        AppUser user = findUser(userId);
        HelpApplication app = new HelpApplication();
        app.setHelpRequest(hr);
        app.setUser(user);
        app.setProposal(request.proposal());
        app.setAppliedAt(LocalDateTime.now());
        app.setSelected(false);
        applicationRepository.save(app);
        return toResponse(hr, userId);
    }

    public List<ApplicationResponse> getRequestApplications(Long requestId) {
        Long userId = currentUserService.getCurrentUserId();
        HelpRequest hr = findRequest(requestId);
        if (!hr.getPoster().getId().equals(userId)) {
            throw ApiException.forbidden("Solo el creador puede ver las aplicaciones.");
        }
        return applicationRepository.findByHelpRequestId(requestId).stream()
                .map(a -> new ApplicationResponse(a.getId(), a.getUser().getId(), a.getUser().getName(),
                        a.getUser().getAvatarUrl(), a.getProposal(), a.getAppliedAt(), a.isSelected()))
                .toList();
    }

    public HelpRequestResponse selectHelper(Long requestId, Long applicationId) {
        Long userId = currentUserService.getCurrentUserId();
        HelpRequest hr = findRequest(requestId);
        if (!hr.getPoster().getId().equals(userId)) {
            throw ApiException.forbidden("Solo el creador puede seleccionar al ayudante.");
        }
        HelpApplication app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> ApiException.notFound("Aplicación no encontrada."));
        app.setSelected(true);
        applicationRepository.save(app);

        AppUser helper = app.getUser();
        helper.setCredits(helper.getCredits() + hr.getCreditsOffered());
        userRepository.save(helper);

        hr.setStatus(HelpRequestStatus.RESOLVED);
        hr.setSelectedApplicationId(applicationId);
        return toResponse(helpRequestRepository.save(hr), userId);
    }

    private HelpRequestResponse toResponse(HelpRequest r, Long currentUserId) {
        boolean applied = currentUserId != null &&
                applicationRepository.findByHelpRequestIdAndUserId(r.getId(), currentUserId).isPresent();
        return new HelpRequestResponse(
                r.getId(), r.getTitle(), r.getDescription(), r.getCreditsOffered(),
                r.getStatus().name(), r.getDeadline(), r.getCreatedAt(),
                r.getPoster().getId(), r.getPoster().getName(), r.getPoster().getAvatarUrl(),
                r.getCategory() != null ? r.getCategory().getId() : null,
                r.getCategory() != null ? r.getCategory().getName() : null,
                applicationRepository.countByHelpRequestId(r.getId()), applied
        );
    }

    private HelpRequest findRequest(Long id) {
        return helpRequestRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Solicitud no encontrada."));
    }

    private AppUser findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Usuario no encontrado."));
    }

    private Long safeCurrentUserId() {
        return currentUserService.getCurrentUserIdOrNull();
    }
}
