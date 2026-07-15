package com.utp.proyecto.services;

import com.utp.proyecto.dto.ApplicationResponse;
import com.utp.proyecto.dto.ApplyRequest;
import com.utp.proyecto.dto.ChallengeResponse;
import com.utp.proyecto.dto.CompanyProfileResponse;
import com.utp.proyecto.dto.CreateChallengeRequest;
import com.utp.proyecto.dto.CreateCompanyProfileRequest;
import com.utp.proyecto.exceptions.ApiException;
import com.utp.proyecto.models.AppUser;
import com.utp.proyecto.models.Challenge;
import com.utp.proyecto.models.ChallengeApplication;
import com.utp.proyecto.models.ChallengeStatus;
import com.utp.proyecto.models.CompanyProfile;
import com.utp.proyecto.repositories.AppUserRepository;
import com.utp.proyecto.repositories.CategoryRepository;
import com.utp.proyecto.repositories.ChallengeApplicationRepository;
import com.utp.proyecto.repositories.ChallengeRepository;
import com.utp.proyecto.repositories.CompanyProfileRepository;
import com.utp.proyecto.security.CurrentUserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeApplicationRepository applicationRepository;
    private final CompanyProfileRepository companyRepository;
    private final AppUserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CurrentUserService currentUserService;

    public ChallengeService(ChallengeRepository challengeRepository,
                            ChallengeApplicationRepository applicationRepository,
                            CompanyProfileRepository companyRepository,
                            AppUserRepository userRepository,
                            CategoryRepository categoryRepository,
                            CurrentUserService currentUserService) {
        this.challengeRepository = challengeRepository;
        this.applicationRepository = applicationRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.currentUserService = currentUserService;
    }

    public CompanyProfileResponse registerCompany(CreateCompanyProfileRequest request) {
        Long userId = currentUserService.getCurrentUserId();
        if (companyRepository.findByUserId(userId).isPresent()) {
            throw ApiException.badRequest("Ya tienes un perfil de empresa registrado.");
        }
        AppUser user = findUser(userId);
        CompanyProfile company = new CompanyProfile();
        company.setUser(user);
        company.setCompanyName(request.companyName());
        company.setRuc(request.ruc());
        company.setSector(request.sector());
        company.setVerified(false);
        company.setCreatedAt(LocalDateTime.now());
        CompanyProfile saved = companyRepository.save(company);
        return toCompanyResponse(saved);
    }

    public CompanyProfileResponse getMyCompanyProfile() {
        Long userId = currentUserService.getCurrentUserId();
        CompanyProfile company = companyRepository.findByUserId(userId)
                .orElseThrow(() -> ApiException.notFound("No tienes perfil de empresa."));
        return toCompanyResponse(company);
    }

    public List<ChallengeResponse> listOpenChallenges() {
        Long userId = safeCurrentUserId();
        return challengeRepository.findByStatus(ChallengeStatus.OPEN).stream()
                .map(c -> toChallengeResponse(c, userId))
                .toList();
    }

    public List<ChallengeResponse> listMyChallenges() {
        Long userId = currentUserService.getCurrentUserId();
        CompanyProfile company = companyRepository.findByUserId(userId)
                .orElseThrow(() -> ApiException.notFound("No tienes perfil de empresa."));
        return challengeRepository.findByCompanyId(company.getId()).stream()
                .map(c -> toChallengeResponse(c, userId))
                .toList();
    }

    public ChallengeResponse createChallenge(CreateChallengeRequest request) {
        Long userId = currentUserService.getCurrentUserId();
        CompanyProfile company = companyRepository.findByUserId(userId)
                .orElseThrow(() -> ApiException.badRequest("Debes registrar un perfil de empresa primero."));
        Challenge challenge = new Challenge();
        challenge.setCompany(company);
        challenge.setTitle(request.title());
        challenge.setDescription(request.description());
        challenge.setCreditsReward(request.creditsReward());
        challenge.setStatus(ChallengeStatus.OPEN);
        challenge.setDeadline(request.deadline());
        challenge.setCreatedAt(LocalDateTime.now());
        if (request.categoryId() != null) {
            challenge.setCategory(categoryRepository.findById(request.categoryId()).orElse(null));
        }
        return toChallengeResponse(challengeRepository.save(challenge), userId);
    }

    public ChallengeResponse applyToChallenge(Long challengeId, ApplyRequest request) {
        Long userId = currentUserService.getCurrentUserId();
        Challenge challenge = findChallenge(challengeId);
        if (!challenge.getStatus().equals(ChallengeStatus.OPEN)) {
            throw ApiException.badRequest("Este desafío ya no está abierto.");
        }
        if (applicationRepository.findByChallengeIdAndUserId(challengeId, userId).isPresent()) {
            throw ApiException.badRequest("Ya aplicaste a este desafío.");
        }
        AppUser user = findUser(userId);
        ChallengeApplication app = new ChallengeApplication();
        app.setChallenge(challenge);
        app.setUser(user);
        app.setProposal(request.proposal());
        app.setAppliedAt(LocalDateTime.now());
        app.setSelected(false);
        applicationRepository.save(app);
        return toChallengeResponse(challenge, userId);
    }

    public List<ApplicationResponse> getChallengeApplications(Long challengeId) {
        Long userId = currentUserService.getCurrentUserId();
        Challenge challenge = findChallenge(challengeId);
        CompanyProfile company = companyRepository.findByUserId(userId)
                .orElseThrow(() -> ApiException.forbidden("Acceso restringido."));
        if (!challenge.getCompany().getId().equals(company.getId())) {
            throw ApiException.forbidden("No eres el dueño de este desafío.");
        }
        return applicationRepository.findByChallengeId(challengeId).stream()
                .map(this::toApplicationResponse)
                .toList();
    }

    public ChallengeResponse selectWinner(Long challengeId, Long applicationId) {
        Long userId = currentUserService.getCurrentUserId();
        Challenge challenge = findChallenge(challengeId);
        CompanyProfile company = companyRepository.findByUserId(userId)
                .orElseThrow(() -> ApiException.forbidden("Acceso restringido."));
        if (!challenge.getCompany().getId().equals(company.getId())) {
            throw ApiException.forbidden("No eres el dueño de este desafío.");
        }
        ChallengeApplication app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> ApiException.notFound("Aplicación no encontrada."));
        app.setSelected(true);
        applicationRepository.save(app);

        AppUser winner = app.getUser();
        winner.setCredits(winner.getCredits() + challenge.getCreditsReward());
        userRepository.save(winner);

        challenge.setStatus(ChallengeStatus.CLOSED);
        challenge.setSelectedApplicationId(applicationId);
        return toChallengeResponse(challengeRepository.save(challenge), userId);
    }

    private ChallengeResponse toChallengeResponse(Challenge c, Long currentUserId) {
        boolean applied = currentUserId != null &&
                applicationRepository.findByChallengeIdAndUserId(c.getId(), currentUserId).isPresent();
        return new ChallengeResponse(
                c.getId(), c.getTitle(), c.getDescription(), c.getCreditsReward(),
                c.getStatus().name(), c.getDeadline(), c.getCreatedAt(),
                c.getCompany().getId(), c.getCompany().getCompanyName(),
                c.getCategory() != null ? c.getCategory().getId() : null,
                c.getCategory() != null ? c.getCategory().getName() : null,
                applicationRepository.countByChallengeId(c.getId()), applied
        );
    }

    private ApplicationResponse toApplicationResponse(ChallengeApplication app) {
        return new ApplicationResponse(app.getId(), app.getUser().getId(), app.getUser().getName(),
                app.getUser().getAvatarUrl(), app.getProposal(), app.getAppliedAt(), app.isSelected());
    }

    private CompanyProfileResponse toCompanyResponse(CompanyProfile c) {
        return new CompanyProfileResponse(c.getId(), c.getUser().getId(), c.getCompanyName(), c.getRuc(), c.getSector(), c.isVerified());
    }

    private Challenge findChallenge(Long id) {
        return challengeRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Desafío no encontrado."));
    }

    private AppUser findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Usuario no encontrado."));
    }

    private Long safeCurrentUserId() {
        return currentUserService.getCurrentUserIdOrNull();
    }
}
