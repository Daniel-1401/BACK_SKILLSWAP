package com.utp.proyecto.repositories;

import com.utp.proyecto.models.ScheduleProposal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleProposalRepository extends JpaRepository<ScheduleProposal, Long> {
}
