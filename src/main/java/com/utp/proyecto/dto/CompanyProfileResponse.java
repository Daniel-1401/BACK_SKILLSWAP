package com.utp.proyecto.dto;

public record CompanyProfileResponse(Long id, Long userId, String companyName, String ruc, String sector, boolean verified) {}
