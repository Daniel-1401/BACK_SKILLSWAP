package com.utp.proyecto.repositories;

import com.utp.proyecto.models.LessonProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LessonProgressRepository extends JpaRepository<LessonProgress, Long> {
    Optional<LessonProgress> findByUserIdAndLessonId(Long userId, Long lessonId);
    List<LessonProgress> findByUserId(Long userId);

    @Query("SELECT COUNT(lp) FROM LessonProgress lp " +
           "JOIN lp.lesson l JOIN l.module m " +
           "WHERE lp.user.id = :userId AND m.course.id = :courseId")
    long countCompletedLessonsForCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);
}
