package com.utp.proyecto.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "schedule_proposals")
public class ScheduleProposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "session_id")
    private LearningSession session;

    @ManyToOne(optional = false)
    @JoinColumn(name = "proposed_by_id")
    private AppUser proposedBy;

    @Column(nullable = false)
    private LocalDate proposedDate;

    @Column(nullable = false)
    private LocalTime proposedTime;

    @Column(length = 2000)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProposalStatus status = ProposalStatus.PENDING;

    public Long getId() { return id; }

    public LearningSession getSession() { return session; }
    public void setSession(LearningSession session) { this.session = session; }

    public AppUser getProposedBy() { return proposedBy; }
    public void setProposedBy(AppUser proposedBy) { this.proposedBy = proposedBy; }

    public LocalDate getProposedDate() { return proposedDate; }
    public void setProposedDate(LocalDate proposedDate) { this.proposedDate = proposedDate; }

    public LocalTime getProposedTime() { return proposedTime; }
    public void setProposedTime(LocalTime proposedTime) { this.proposedTime = proposedTime; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public ProposalStatus getStatus() { return status; }
    public void setStatus(ProposalStatus status) { this.status = status; }
}
