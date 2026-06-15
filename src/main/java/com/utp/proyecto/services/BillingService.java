package com.utp.proyecto.services;

import com.utp.proyecto.dto.CreditBalanceResponse;
import com.utp.proyecto.dto.CreditHistoryResponse;
import com.utp.proyecto.dto.PaymentCheckoutRequest;
import com.utp.proyecto.dto.PaymentCheckoutResponse;
import com.utp.proyecto.dto.PaymentConfirmRequest;
import com.utp.proyecto.dto.PaymentConfirmResponse;
import com.utp.proyecto.dto.PaymentHistoryResponse;
import com.utp.proyecto.dto.PlanResponse;
import com.utp.proyecto.exceptions.ApiException;
import com.utp.proyecto.models.AppUser;
import com.utp.proyecto.models.CreditTransaction;
import com.utp.proyecto.models.Payment;
import com.utp.proyecto.models.PaymentStatus;
import com.utp.proyecto.models.Plan;
import com.utp.proyecto.repositories.AppUserRepository;
import com.utp.proyecto.repositories.CreditTransactionRepository;
import com.utp.proyecto.repositories.PaymentRepository;
import com.utp.proyecto.repositories.PlanRepository;
import com.utp.proyecto.security.CurrentUserService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class BillingService {
    private final PlanRepository planRepository;
    private final PaymentRepository paymentRepository;
    private final CreditTransactionRepository creditRepository;
    private final AppUserRepository userRepository;
    private final CurrentUserService currentUserService;

    public BillingService(
            PlanRepository planRepository,
            PaymentRepository paymentRepository,
            CreditTransactionRepository creditRepository,
            AppUserRepository userRepository,
            CurrentUserService currentUserService
    ) {
        this.planRepository = planRepository;
        this.paymentRepository = paymentRepository;
        this.creditRepository = creditRepository;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
    }

    public List<PlanResponse> getPlans() {
        return planRepository.findAll().stream().map(this::toPlanResponse).toList();
    }

    public CreditBalanceResponse getBalance() {
        AppUser user = currentUserService.getCurrentUser();
        return new CreditBalanceResponse(user.getId(), user.getCredits());
    }

    public List<CreditHistoryResponse> getCreditHistory() {
        return creditRepository.findByUserIdOrderByCreatedAtDesc(currentUserService.getCurrentUserId()).stream()
                .map(transaction -> new CreditHistoryResponse(
                        transaction.getId(),
                        transaction.getAmount(),
                        transaction.getBalanceAfter(),
                        transaction.getDescription(),
                        transaction.getCreatedAt().toString()
                ))
                .toList();
    }

    public PaymentCheckoutResponse checkout(PaymentCheckoutRequest request) {
        Plan plan = planRepository.findById(request.planId())
                .orElseThrow(() -> ApiException.notFound("Plan no encontrado."));

        Payment payment = new Payment();
        payment.setUser(currentUserService.getCurrentUser());
        payment.setPlan(plan);
        payment.setPaymentMethod(request.paymentMethod() == null ? "card" : request.paymentMethod());
        payment.setAmount(calculateTotal(plan.getPrice()));
        Payment saved = paymentRepository.save(payment);

        return new PaymentCheckoutResponse(saved.getId(), "pending", saved.getAmount(), saved.getCurrency());
    }

    public PaymentConfirmResponse confirm(PaymentConfirmRequest request) {
        Payment payment = paymentRepository.findById(request.paymentId())
                .orElseThrow(() -> ApiException.notFound("Pago no encontrado."));
        AppUser currentUser = currentUserService.getCurrentUser();
        if (!payment.getUser().getId().equals(currentUser.getId())) {
            throw ApiException.forbidden("No tienes acceso a este pago.");
        }
        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw ApiException.badRequest("El pago ya fue procesado.");
        }

        int creditsToAdd = payment.getPlan().getCredits() == null ? 0 : payment.getPlan().getCredits();
        currentUser.setCredits(currentUser.getCredits() + creditsToAdd);
        userRepository.save(currentUser);

        payment.setStatus(PaymentStatus.APPROVED);
        payment.setProviderTransactionId(request.providerTransactionId());
        payment.setCreditsAdded(creditsToAdd);
        paymentRepository.save(payment);

        if (creditsToAdd > 0) {
            CreditTransaction transaction = new CreditTransaction();
            transaction.setUser(currentUser);
            transaction.setAmount(creditsToAdd);
            transaction.setBalanceAfter(currentUser.getCredits());
            transaction.setDescription("Compra de " + payment.getPlan().getName());
            creditRepository.save(transaction);
        }

        return new PaymentConfirmResponse(payment.getId(), "approved", creditsToAdd, currentUser.getCredits());
    }

    public List<PaymentHistoryResponse> getPaymentHistory() {
        return paymentRepository.findByUserIdOrderByCreatedAtDesc(currentUserService.getCurrentUserId()).stream()
                .map(payment -> new PaymentHistoryResponse(
                        payment.getId(),
                        payment.getPlan().getId(),
                        payment.getStatus().name().toLowerCase(Locale.ROOT),
                        payment.getAmount(),
                        payment.getCurrency(),
                        payment.getCreditsAdded(),
                        payment.getCreatedAt().toString()
                ))
                .toList();
    }

    private PlanResponse toPlanResponse(Plan plan) {
        return new PlanResponse(
                plan.getId(),
                plan.getName(),
                plan.getPrice(),
                plan.getBillingPeriod(),
                plan.getCredits(),
                Arrays.stream(plan.getFeatures().split("\\|")).toList()
        );
    }

    private BigDecimal calculateTotal(BigDecimal price) {
        return price.multiply(new BigDecimal("1.18")).setScale(2, RoundingMode.HALF_UP);
    }
}
