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

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "learning_sessions")
public class LearningSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exchange_request_id")
    private ExchangeRequest exchangeRequest;

    @ManyToOne(optional = false)
    @JoinColumn(name = "teacher_id")
    private AppUser teacher;

    @ManyToOne(optional = false)
    @JoinColumn(name = "learner_id")
    private AppUser learner;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false)
    private SessionStatus status;

    private LocalDate sessionDate;
    private LocalTime sessionTime;
    private String duration;

    public Long getId() { return id; }

    public ExchangeRequest getExchangeRequest() { return exchangeRequest; }
    public void setExchangeRequest(ExchangeRequest exchangeRequest) { this.exchangeRequest = exchangeRequest; }

    public AppUser getTeacher() { return teacher; }
    public void setTeacher(AppUser teacher) { this.teacher = teacher; }

    public AppUser getLearner() { return learner; }
    public void setLearner(AppUser learner) { this.learner = learner; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public SessionStatus getStatus() { return status; }
    public void setStatus(SessionStatus status) { this.status = status; }

    public LocalDate getSessionDate() { return sessionDate; }
    public void setSessionDate(LocalDate sessionDate) { this.sessionDate = sessionDate; }

    public LocalTime getSessionTime() { return sessionTime; }
    public void setSessionTime(LocalTime sessionTime) { this.sessionTime = sessionTime; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
}
