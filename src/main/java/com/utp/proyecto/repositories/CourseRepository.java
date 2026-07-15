package com.utp.proyecto.repositories;

import com.utp.proyecto.models.Course;
import com.utp.proyecto.models.CourseStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByInstructorId(Long instructorId);
    List<Course> findByStatus(CourseStatus status);
}
