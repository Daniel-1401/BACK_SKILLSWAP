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

import java.time.LocalDateTime;

@Entity
@Table(name = "exchange_requests")
public class ExchangeRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "requester_id")
    private AppUser requester;

    @ManyToOne(optional = false)
    @JoinColumn(name = "target_user_id")
    private AppUser targetUser;

    @ManyToOne(optional = false)
    @JoinColumn(name = "skill_wanted_id")
    private Skill skillWanted;

    @ManyToOne(optional = false)
    @JoinColumn(name = "skill_offered_id")
    private Skill skillOffered;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExchangeStatus status = ExchangeStatus.PENDING;

    @Column(length = 2000)
    private String message;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }

    public AppUser getRequester() { return requester; }
    public void setRequester(AppUser requester) { this.requester = requester; }

    public AppUser getTargetUser() { return targetUser; }
    public void setTargetUser(AppUser targetUser) { this.targetUser = targetUser; }

    public Skill getSkillWanted() { return skillWanted; }
    public void setSkillWanted(Skill skillWanted) { this.skillWanted = skillWanted; }

    public Skill getSkillOffered() { return skillOffered; }
    public void setSkillOffered(Skill skillOffered) { this.skillOffered = skillOffered; }

    public ExchangeStatus getStatus() { return status; }
    public void setStatus(ExchangeStatus status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
