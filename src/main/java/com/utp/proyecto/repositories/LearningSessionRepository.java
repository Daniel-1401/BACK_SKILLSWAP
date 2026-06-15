package com.utp.proyecto.repositories;

import com.utp.proyecto.models.LearningSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearningSessionRepository extends JpaRepository<LearningSession, Long> {
    List<LearningSession> findByTeacherIdOrLearnerIdOrderByIdDesc(Long teacherId, Long learnerId);
}
