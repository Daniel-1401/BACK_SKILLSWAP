package com.utp.proyecto.dto;

public record SkillFeaturedResponse(Long id, String name, Long tutorId, String tutor, String description, double subscriptionCost, String imageUrl) {
}
