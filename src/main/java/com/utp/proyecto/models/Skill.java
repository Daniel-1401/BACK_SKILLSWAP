package com.utp.proyecto.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "skills")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private String tutor;

    @Column(nullable = false)
    private double subscriptionCost;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Skill() {}

    public Skill(String name, String description, String tutor, double subscriptionCost, Category category) {
        this.name = name;
        this.description = description;
        this.tutor = tutor;
        this.subscriptionCost = subscriptionCost;
        this.category = category;
    }

    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTutor() { return tutor; }
    public void setTutor(String tutor) { this.tutor = tutor; }

    public double getSubscriptionCost() { return subscriptionCost; }
    public void setSubscriptionCost(double subscriptionCost) { this.subscriptionCost = subscriptionCost; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}
