package com.utp.proyecto.repositories;

import com.utp.proyecto.models.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByModuleIdOrderByOrderIndex(Long moduleId);
    long countByModuleId(Long moduleId);
}
