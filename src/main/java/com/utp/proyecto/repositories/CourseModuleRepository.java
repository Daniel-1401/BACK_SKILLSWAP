package com.utp.proyecto.repositories;

import com.utp.proyecto.models.CourseModule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseModuleRepository extends JpaRepository<CourseModule, Long> {
    List<CourseModule> findByCourseIdOrderByOrderIndex(Long courseId);
}
