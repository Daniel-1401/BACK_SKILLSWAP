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
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "instructor_id")
    private AppUser instructor;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(optional = true)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false)
    private int priceCredits;

    @Column(length = 1000)
    private String coverUrl;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false)
    private CourseLevel level;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false)
    private CourseStatus status;

    @Column(nullable = false)
    private boolean isCertifiable;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Long getId() { return id; }

    public AppUser getInstructor() { return instructor; }
    public void setInstructor(AppUser instructor) { this.instructor = instructor; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public int getPriceCredits() { return priceCredits; }
    public void setPriceCredits(int priceCredits) { this.priceCredits = priceCredits; }

    public String getCoverUrl() { return coverUrl; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }

    public CourseLevel getLevel() { return level; }
    public void setLevel(CourseLevel level) { this.level = level; }

    public CourseStatus getStatus() { return status; }
    public void setStatus(CourseStatus status) { this.status = status; }

    public boolean isCertifiable() { return isCertifiable; }
    public void setCertifiable(boolean certifiable) { isCertifiable = certifiable; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
