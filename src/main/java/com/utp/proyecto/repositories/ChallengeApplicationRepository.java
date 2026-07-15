package com.utp.proyecto.repositories;

import com.utp.proyecto.models.ChallengeApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChallengeApplicationRepository extends JpaRepository<ChallengeApplication, Long> {
    List<ChallengeApplication> findByChallengeId(Long challengeId);
    Optional<ChallengeApplication> findByChallengeIdAndUserId(Long challengeId, Long userId);
    long countByChallengeId(Long challengeId);
}
