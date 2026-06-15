package com.utp.proyecto.services;

import com.utp.proyecto.dto.FinishSessionResponse;
import com.utp.proyecto.dto.RoomResponse;
import com.utp.proyecto.dto.ScheduleProposalRequest;
import com.utp.proyecto.dto.ScheduleProposalResponse;
import com.utp.proyecto.dto.SessionResponse;
import com.utp.proyecto.exceptions.ApiException;
import com.utp.proyecto.models.AppUser;
import com.utp.proyecto.models.CreditTransaction;
import com.utp.proyecto.models.LearningSession;
import com.utp.proyecto.models.ProposalStatus;
import com.utp.proyecto.models.ScheduleProposal;
import com.utp.proyecto.models.SessionStatus;
import com.utp.proyecto.repositories.AppUserRepository;
import com.utp.proyecto.repositories.CreditTransactionRepository;
import com.utp.proyecto.repositories.LearningSessionRepository;
import com.utp.proyecto.repositories.ScheduleProposalRepository;
import com.utp.proyecto.security.CurrentUserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Service
public class SessionService {
    private final LearningSessionRepository sessionRepository;
    private final ScheduleProposalRepository proposalRepository;
    private final AppUserRepository userRepository;
    private final CreditTransactionRepository creditRepository;
    private final CurrentUserService currentUserService;

    public SessionService(
            LearningSessionRepository sessionRepository,
            ScheduleProposalRepository proposalRepository,
            AppUserRepository userRepository,
            CreditTransactionRepository creditRepository,
            CurrentUserService currentUserService
    ) {
        this.sessionRepository = sessionRepository;
        this.proposalRepository = proposalRepository;
        this.userRepository = userRepository;
        this.creditRepository = creditRepository;
        this.currentUserService = currentUserService;
    }

    public List<SessionResponse> list(String status, String from, String to) {
        Long currentUserId = currentUserService.getCurrentUserId();
        return sessionRepository.findByTeacherIdOrLearnerIdOrderByIdDesc(currentUserId, currentUserId).stream()
                .filter(session -> status == null || session.getStatus().name().equalsIgnoreCase(status))
                .filter(session -> inRange(session, from, to))
                .map(session -> toResponse(session, currentUserId))
                .toList();
    }

    public SessionResponse getById(Long id) {
        LearningSession session = findSessionForCurrentUser(id);
        return toResponse(session, currentUserService.getCurrentUserId());
    }

    public ScheduleProposalResponse proposeSchedule(Long sessionId, ScheduleProposalRequest request) {
        LearningSession session = findSessionForCurrentUser(sessionId);
        ScheduleProposal proposal = new ScheduleProposal();
        proposal.setSession(session);
        proposal.setProposedBy(currentUserService.getCurrentUser());
        proposal.setProposedDate(LocalDate.parse(request.date()));
        proposal.setProposedTime(LocalTime.parse(request.time()));
        proposal.setMessage(request.message());
        ScheduleProposal saved = proposalRepository.save(proposal);

        return new ScheduleProposalResponse(
                saved.getId(),
                session.getId(),
                saved.getProposedDate().toString(),
                saved.getProposedTime().toString(),
                saved.getStatus().name().toLowerCase(Locale.ROOT)
        );
    }

    public ScheduleProposalResponse acceptProposal(Long sessionId, Long proposalId) {
        LearningSession session = findSessionForCurrentUser(sessionId);
        ScheduleProposal proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> ApiException.notFound("Propuesta no encontrada."));
        if (!proposal.getSession().getId().equals(session.getId())) {
            throw ApiException.badRequest("La propuesta no pertenece a esta sesion.");
        }

        proposal.setStatus(ProposalStatus.ACCEPTED);
        proposalRepository.save(proposal);
        session.setSessionDate(proposal.getProposedDate());
        session.setSessionTime(proposal.getProposedTime());
        session.setDuration("1 hora");
        session.setStatus(SessionStatus.SCHEDULED);
        sessionRepository.save(session);

        return new ScheduleProposalResponse(
                proposal.getId(),
                session.getId(),
                proposal.getProposedDate().toString(),
                proposal.getProposedTime().toString(),
                "accepted"
        );
    }

    public SessionResponse reschedule(Long sessionId, ScheduleProposalRequest request) {
        LearningSession session = findSessionForCurrentUser(sessionId);
        session.setSessionDate(LocalDate.parse(request.date()));
        session.setSessionTime(LocalTime.parse(request.time()));
        session.setStatus(SessionStatus.SCHEDULED);
        sessionRepository.save(session);
        return toResponse(session, currentUserService.getCurrentUserId());
    }

    public RoomResponse start(Long sessionId) {
        LearningSession session = findSessionForCurrentUser(sessionId);
        session.setStatus(SessionStatus.STARTED);
        sessionRepository.save(session);
        return new RoomResponse(session.getId(), "https://meet.example.com/skillswap-session-" + session.getId(), "started");
    }

    public FinishSessionResponse finish(Long sessionId) {
        LearningSession session = findSessionForCurrentUser(sessionId);
        session.setStatus(SessionStatus.FINISHED);
        sessionRepository.save(session);

        AppUser teacher = session.getTeacher();
        teacher.setCredits(teacher.getCredits() + 1);
        userRepository.save(teacher);

        CreditTransaction transaction = new CreditTransaction();
        transaction.setUser(teacher);
        transaction.setAmount(1);
        transaction.setBalanceAfter(teacher.getCredits());
        transaction.setDescription("Credito ganado por sesion finalizada");
        creditRepository.save(transaction);

        return new FinishSessionResponse(session.getId(), "finished", 1, teacher.getCredits());
    }

    public LearningSession findSessionForCurrentUser(Long id) {
        Long currentUserId = currentUserService.getCurrentUserId();
        LearningSession session = sessionRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Sesion no encontrada."));
        if (!session.getTeacher().getId().equals(currentUserId) && !session.getLearner().getId().equals(currentUserId)) {
            throw ApiException.forbidden("No tienes acceso a esta sesion.");
        }
        return session;
    }

    private boolean inRange(LearningSession session, String from, String to) {
        if (session.getSessionDate() == null) {
            return true;
        }
        if (from != null && !from.isBlank() && session.getSessionDate().isBefore(LocalDate.parse(from))) {
            return false;
        }
        return to == null || to.isBlank() || !session.getSessionDate().isAfter(LocalDate.parse(to));
    }

    private SessionResponse toResponse(LearningSession session, Long currentUserId) {
        boolean teaches = session.getTeacher().getId().equals(currentUserId);
        AppUser person = teaches ? session.getLearner() : session.getTeacher();
        String month = session.getSessionDate() == null
                ? null
                : session.getSessionDate().getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase(Locale.ROOT);
        String day = session.getSessionDate() == null ? null : String.valueOf(session.getSessionDate().getDayOfMonth());
        String time = session.getSessionTime() == null ? null : session.getSessionTime().toString();

        return new SessionResponse(
                session.getId(),
                teaches ? "teaches" : "learns",
                session.getStatus().name().toLowerCase(Locale.ROOT),
                session.getTitle(),
                teaches ? "Alumno" : "Tutor",
                person.getName(),
                month,
                day,
                session.getSessionDate() == null ? null : session.getSessionDate().toString(),
                time,
                session.getDuration()
        );
    }
}
