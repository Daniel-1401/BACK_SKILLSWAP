package com.utp.proyecto.repositories;

import com.utp.proyecto.models.HelpApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HelpApplicationRepository extends JpaRepository<HelpApplication, Long> {
    List<HelpApplication> findByHelpRequestId(Long helpRequestId);
    Optional<HelpApplication> findByHelpRequestIdAndUserId(Long helpRequestId, Long userId);
    long countByHelpRequestId(Long helpRequestId);
}
