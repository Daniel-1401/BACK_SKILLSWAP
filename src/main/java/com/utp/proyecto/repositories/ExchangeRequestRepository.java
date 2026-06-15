package com.utp.proyecto.repositories;

import com.utp.proyecto.models.ExchangeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExchangeRequestRepository extends JpaRepository<ExchangeRequest, Long> {
    List<ExchangeRequest> findByRequesterIdOrTargetUserIdOrderByCreatedAtDesc(Long requesterId, Long targetUserId);
}
