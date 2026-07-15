package com.utp.proyecto.repositories;

import com.utp.proyecto.models.Certification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CertificationRepository extends JpaRepository<Certification, Long> {
    List<Certification> findByUserId(Long userId);
    Optional<Certification> findByCertificateCode(String certificateCode);
    boolean existsByUserIdAndCourseId(Long userId, Long courseId);
}
