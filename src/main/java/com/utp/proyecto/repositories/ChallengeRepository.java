package com.utp.proyecto.repositories;

import com.utp.proyecto.models.Challenge;
import com.utp.proyecto.models.ChallengeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    List<Challenge> findByStatus(ChallengeStatus status);
    List<Challenge> findByCompanyId(Long companyId);
}
