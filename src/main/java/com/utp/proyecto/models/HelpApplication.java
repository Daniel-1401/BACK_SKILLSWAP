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
@Table(name = "help_applications")
public class HelpApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "help_request_id")
    private HelpRequest helpRequest;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private AppUser user;

    @Column(columnDefinition = "TEXT")
    private String proposal;

    @Column(nullable = false)
    private LocalDateTime appliedAt;

    @Column(nullable = false)
    private boolean selected;

    public Long getId() { return id; }

    public HelpRequest getHelpRequest() { return helpRequest; }
    public void setHelpRequest(HelpRequest helpRequest) { this.helpRequest = helpRequest; }

    public AppUser getUser() { return user; }
    public void setUser(AppUser user) { this.user = user; }

    public String getProposal() { return proposal; }
    public void setProposal(String proposal) { this.proposal = proposal; }

    public LocalDateTime getAppliedAt() { return appliedAt; }
    public void setAppliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; }

    public boolean isSelected() { return selected; }
    public void setSelected(boolean selected) { this.selected = selected; }
}
