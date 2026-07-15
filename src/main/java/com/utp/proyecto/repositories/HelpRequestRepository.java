package com.utp.proyecto.repositories;

import com.utp.proyecto.models.HelpRequest;
import com.utp.proyecto.models.HelpRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HelpRequestRepository extends JpaRepository<HelpRequest, Long> {
    List<HelpRequest> findByStatus(HelpRequestStatus status);
    List<HelpRequest> findByPosterId(Long posterId);
}
