package com.utp.proyecto.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "conversations")
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "participant_one_id")
    private AppUser participantOne;

    @ManyToOne(optional = false)
    @JoinColumn(name = "participant_two_id")
    private AppUser participantTwo;

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Long getId() { return id; }

    public AppUser getParticipantOne() { return participantOne; }
    public void setParticipantOne(AppUser participantOne) { this.participantOne = participantOne; }

    public AppUser getParticipantTwo() { return participantTwo; }
    public void setParticipantTwo(AppUser participantTwo) { this.participantTwo = participantTwo; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
