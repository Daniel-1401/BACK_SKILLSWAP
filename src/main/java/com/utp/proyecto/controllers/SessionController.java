package com.utp.proyecto.controllers;

import com.utp.proyecto.dto.FinishSessionResponse;
import com.utp.proyecto.dto.ReviewRequest;
import com.utp.proyecto.dto.ReviewResponse;
import com.utp.proyecto.dto.RoomResponse;
import com.utp.proyecto.dto.ScheduleProposalRequest;
import com.utp.proyecto.dto.ScheduleProposalResponse;
import com.utp.proyecto.dto.SessionResponse;
import com.utp.proyecto.services.ReviewService;
import com.utp.proyecto.services.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sessions")
public class SessionController {
    private final SessionService sessionService;
    private final ReviewService reviewService;

    public SessionController(SessionService sessionService, ReviewService reviewService) {
        this.sessionService = sessionService;
        this.reviewService = reviewService;
    }

    @GetMapping
    public List<SessionResponse> list(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to
    ) {
        return sessionService.list(status, from, to);
    }

    @GetMapping("/{id}")
    public SessionResponse getById(@PathVariable Long id) {
        return sessionService.getById(id);
    }

    @PostMapping("/{id}/schedule-proposals")
    public ResponseEntity<ScheduleProposalResponse> proposeSchedule(
            @PathVariable Long id,
            @RequestBody ScheduleProposalRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sessionService.proposeSchedule(id, request));
    }

    @PostMapping("/{id}/schedule-proposals/{proposalId}/accept")
    public ScheduleProposalResponse acceptProposal(@PathVariable Long id, @PathVariable Long proposalId) {
        return sessionService.acceptProposal(id, proposalId);
    }

    @PostMapping("/{id}/reschedule")
    public SessionResponse reschedule(@PathVariable Long id, @RequestBody ScheduleProposalRequest request) {
        return sessionService.reschedule(id, request);
    }

    @PostMapping("/{id}/start")
    public RoomResponse start(@PathVariable Long id) {
        return sessionService.start(id);
    }

    @PostMapping("/{id}/finish")
    public FinishSessionResponse finish(@PathVariable Long id) {
        return sessionService.finish(id);
    }

    @PostMapping("/{id}/reviews")
    public ResponseEntity<ReviewResponse> createReview(@PathVariable Long id, @RequestBody ReviewRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.create(id, request));
    }
}
