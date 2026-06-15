package com.utp.proyecto.services;

import com.utp.proyecto.dto.CreateExchangeRequest;
import com.utp.proyecto.dto.CreateExchangeResponse;
import com.utp.proyecto.dto.ExchangeActionResponse;
import com.utp.proyecto.dto.ExchangeResponse;
import com.utp.proyecto.exceptions.ApiException;
import com.utp.proyecto.models.AppUser;
import com.utp.proyecto.models.ExchangeRequest;
import com.utp.proyecto.models.ExchangeStatus;
import com.utp.proyecto.models.LearningSession;
import com.utp.proyecto.models.SessionStatus;
import com.utp.proyecto.models.Skill;
import com.utp.proyecto.repositories.AppUserRepository;
import com.utp.proyecto.repositories.ExchangeRequestRepository;
import com.utp.proyecto.repositories.LearningSessionRepository;
import com.utp.proyecto.repositories.SkillRepository;
import com.utp.proyecto.security.CurrentUserService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
public class ExchangeService {
    private final ExchangeRequestRepository exchangeRepository;
    private final LearningSessionRepository sessionRepository;
    private final AppUserRepository userRepository;
    private final SkillRepository skillRepository;
    private final CurrentUserService currentUserService;

    public ExchangeService(
            ExchangeRequestRepository exchangeRepository,
            LearningSessionRepository sessionRepository,
            AppUserRepository userRepository,
            SkillRepository skillRepository,
            CurrentUserService currentUserService
    ) {
        this.exchangeRepository = exchangeRepository;
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
        this.currentUserService = currentUserService;
    }

    public List<ExchangeResponse> list(String type, String status) {
        Long currentUserId = currentUserService.getCurrentUserId();
        return exchangeRepository.findByRequesterIdOrTargetUserIdOrderByCreatedAtDesc(currentUserId, currentUserId).stream()
                .filter(exchange -> matchesType(exchange, type, currentUserId))
                .filter(exchange -> matchesStatus(exchange, status))
                .map(exchange -> toResponse(exchange, currentUserId))
                .toList();
    }

    public CreateExchangeResponse create(CreateExchangeRequest request) {
        AppUser requester = currentUserService.getCurrentUser();
        AppUser target = userRepository.findById(request.targetUserId())
                .orElseThrow(() -> ApiException.notFound("Usuario destino no encontrado."));
        Skill wanted = skillRepository.findById(request.skillWantedId())
                .orElseThrow(() -> ApiException.notFound("Habilidad solicitada no encontrada."));
        Skill offered = skillRepository.findById(request.skillOfferedId())
                .orElseThrow(() -> ApiException.notFound("Habilidad ofrecida no encontrada."));

        ExchangeRequest exchange = new ExchangeRequest();
        exchange.setRequester(requester);
        exchange.setTargetUser(target);
        exchange.setSkillWanted(wanted);
        exchange.setSkillOffered(offered);
        exchange.setMessage(request.message());

        ExchangeRequest saved = exchangeRepository.save(exchange);
        return new CreateExchangeResponse(
                saved.getId(),
                "sent",
                saved.getStatus().name().toLowerCase(Locale.ROOT),
                target.getId(),
                wanted.getId(),
                offered.getId(),
                saved.getMessage()
        );
    }

    public ExchangeActionResponse accept(Long id) {
        Long currentUserId = currentUserService.getCurrentUserId();
        ExchangeRequest exchange = findExchange(id);
        if (!exchange.getTargetUser().getId().equals(currentUserId)) {
            throw ApiException.forbidden("Solo el destinatario puede aceptar este intercambio.");
        }
        if (exchange.getStatus() != ExchangeStatus.PENDING) {
            throw ApiException.badRequest("El intercambio no esta pendiente.");
        }

        exchange.setStatus(ExchangeStatus.ACCEPTED);
        exchangeRepository.save(exchange);

        LearningSession session = new LearningSession();
        session.setExchangeRequest(exchange);
        session.setTeacher(exchange.getTargetUser());
        session.setLearner(exchange.getRequester());
        session.setTitle("Clase de " + exchange.getSkillWanted().getName());
        session.setStatus(SessionStatus.PENDING);
        LearningSession savedSession = sessionRepository.save(session);

        return new ExchangeActionResponse(exchange.getId(), "accepted", savedSession.getId());
    }

    public ExchangeActionResponse reject(Long id) {
        Long currentUserId = currentUserService.getCurrentUserId();
        ExchangeRequest exchange = findExchange(id);
        if (!exchange.getTargetUser().getId().equals(currentUserId)) {
            throw ApiException.forbidden("Solo el destinatario puede rechazar este intercambio.");
        }
        exchange.setStatus(ExchangeStatus.REJECTED);
        exchangeRepository.save(exchange);
        return new ExchangeActionResponse(exchange.getId(), "rejected", null);
    }

    public void cancel(Long id) {
        Long currentUserId = currentUserService.getCurrentUserId();
        ExchangeRequest exchange = findExchange(id);
        if (!exchange.getRequester().getId().equals(currentUserId)) {
            throw ApiException.forbidden("Solo el solicitante puede cancelar este intercambio.");
        }
        exchange.setStatus(ExchangeStatus.CANCELLED);
        exchangeRepository.save(exchange);
    }

    private ExchangeRequest findExchange(Long id) {
        return exchangeRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Intercambio no encontrado."));
    }

    private boolean matchesType(ExchangeRequest exchange, String type, Long currentUserId) {
        if (type == null || type.isBlank()) {
            return true;
        }
        boolean received = exchange.getTargetUser().getId().equals(currentUserId);
        return ("received".equalsIgnoreCase(type) && received) || ("sent".equalsIgnoreCase(type) && !received);
    }

    private boolean matchesStatus(ExchangeRequest exchange, String status) {
        if (status == null || status.isBlank()) {
            return true;
        }
        return exchange.getStatus().name().equalsIgnoreCase(status);
    }

    private ExchangeResponse toResponse(ExchangeRequest exchange, Long currentUserId) {
        boolean received = exchange.getTargetUser().getId().equals(currentUserId);
        AppUser person = received ? exchange.getRequester() : exchange.getTargetUser();
        return new ExchangeResponse(
                exchange.getId(),
                received ? "received" : "sent",
                person.getFullName(),
                person.getAvatarUrl(),
                toTimeLabel(exchange.getCreatedAt()),
                exchange.getStatus().name().toLowerCase(Locale.ROOT),
                toStatusLabel(exchange.getStatus(), received),
                exchange.getSkillWanted().getName(),
                exchange.getSkillOffered().getName(),
                exchange.getMessage()
        );
    }

    private String toStatusLabel(ExchangeStatus status, boolean received) {
        return switch (status) {
            case PENDING -> received ? "Pendiente" : "Esperando Respuesta";
            case ACCEPTED -> "Aceptado";
            case REJECTED -> "Rechazado";
            case CANCELLED -> "Cancelado";
        };
    }

    private String toTimeLabel(LocalDateTime createdAt) {
        long hours = Duration.between(createdAt, LocalDateTime.now()).toHours();
        if (hours < 1) {
            return "Hace unos minutos";
        }
        if (hours < 24) {
            return "Hace " + hours + " horas";
        }
        return "Enviada hace " + (hours / 24) + " dias";
    }
}
