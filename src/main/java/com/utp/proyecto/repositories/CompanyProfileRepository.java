package com.utp.proyecto.repositories;

import com.utp.proyecto.models.CompanyProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyProfileRepository extends JpaRepository<CompanyProfile, Long> {
    Optional<CompanyProfile> findByUserId(Long userId);
}
