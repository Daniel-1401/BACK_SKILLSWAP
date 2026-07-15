package com.utp.proyecto.services;

import com.utp.proyecto.dto.CertificationResponse;
import com.utp.proyecto.dto.IssueCertificationRequest;
import com.utp.proyecto.exceptions.ApiException;
import com.utp.proyecto.models.AppUser;
import com.utp.proyecto.models.Certification;
import com.utp.proyecto.models.Course;
import com.utp.proyecto.repositories.AppUserRepository;
import com.utp.proyecto.repositories.CertificationRepository;
import com.utp.proyecto.repositories.CourseRepository;
import com.utp.proyecto.repositories.EnrollmentRepository;
import com.utp.proyecto.repositories.LessonProgressRepository;
import com.utp.proyecto.repositories.CourseModuleRepository;
import com.utp.proyecto.repositories.LessonRepository;
import com.utp.proyecto.security.CurrentUserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CertificationService {

    private final CertificationRepository certificationRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final LessonProgressRepository progressRepository;
    private final CourseModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;
    private final AppUserRepository userRepository;
    private final CurrentUserService currentUserService;

    public CertificationService(CertificationRepository certificationRepository,
                                CourseRepository courseRepository,
                                EnrollmentRepository enrollmentRepository,
                                LessonProgressRepository progressRepository,
                                CourseModuleRepository moduleRepository,
                                LessonRepository lessonRepository,
                                AppUserRepository userRepository,
                                CurrentUserService currentUserService) {
        this.certificationRepository = certificationRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.progressRepository = progressRepository;
        this.moduleRepository = moduleRepository;
        this.lessonRepository = lessonRepository;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
    }

    public CertificationResponse issueCertification(IssueCertificationRequest request) {
        Long userId = currentUserService.getCurrentUserId();
        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> ApiException.notFound("Curso no encontrado."));

        if (!course.isCertifiable()) {
            throw ApiException.badRequest("Este curso no tiene certificación disponible.");
        }

        enrollmentRepository.findByStudentIdAndCourseId(userId, request.courseId())
                .orElseThrow(() -> ApiException.badRequest("No estás inscrito en este curso."));

        long total = moduleRepository.findByCourseIdOrderByOrderIndex(request.courseId()).stream()
                .mapToLong(m -> lessonRepository.countByModuleId(m.getId())).sum();
        long completed = progressRepository.countCompletedLessonsForCourse(userId, request.courseId());
        if (total == 0 || completed < total) {
            throw ApiException.badRequest("Debes completar el 100% del curso para obtener la certificación.");
        }

        if (certificationRepository.existsByUserIdAndCourseId(userId, request.courseId())) {
            throw ApiException.badRequest("Ya tienes una certificación para este curso.");
        }

        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> ApiException.notFound("Usuario no encontrado."));

        Certification cert = new Certification();
        cert.setUser(user);
        cert.setCourse(course);
        cert.setFullName(request.fullName());
        cert.setDni(request.dni());
        cert.setIssuedAt(LocalDateTime.now());
        cert.setCertificateCode(UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase());

        return toResponse(certificationRepository.save(cert));
    }

    public List<CertificationResponse> getMyCertifications() {
        Long userId = currentUserService.getCurrentUserId();
        return certificationRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    public CertificationResponse verifyByCode(String code) {
        Certification cert = certificationRepository.findByCertificateCode(code)
                .orElseThrow(() -> ApiException.notFound("Certificado no encontrado."));
        return toResponse(cert);
    }

    private CertificationResponse toResponse(Certification c) {
        return new CertificationResponse(
                c.getId(), c.getUser().getId(), c.getUser().getName(),
                c.getCourse().getId(), c.getCourse().getTitle(),
                c.getFullName(), c.getDni(), c.getIssuedAt(), c.getCertificateCode()
        );
    }
}
