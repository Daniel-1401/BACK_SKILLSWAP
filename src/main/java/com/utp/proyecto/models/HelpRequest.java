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

import java.time.LocalDateTime;

@Entity
@Table(name = "help_requests")
public class HelpRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "poster_id")
    private AppUser poster;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(optional = true)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false)
    private int creditsOffered;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false)
    private HelpRequestStatus status;

    private LocalDateTime deadline;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "selected_application_id")
    private Long selectedApplicationId;

    public Long getId() { return id; }

    public AppUser getPoster() { return poster; }
    public void setPoster(AppUser poster) { this.poster = poster; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public int getCreditsOffered() { return creditsOffered; }
    public void setCreditsOffered(int creditsOffered) { this.creditsOffered = creditsOffered; }

    public HelpRequestStatus getStatus() { return status; }
    public void setStatus(HelpRequestStatus status) { this.status = status; }

    public LocalDateTime getDeadline() { return deadline; }
    public void setDeadline(LocalDateTime deadline) { this.deadline = deadline; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Long getSelectedApplicationId() { return selectedApplicationId; }
    public void setSelectedApplicationId(Long selectedApplicationId) { this.selectedApplicationId = selectedApplicationId; }
}
