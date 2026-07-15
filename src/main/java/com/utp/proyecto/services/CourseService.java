package com.utp.proyecto.services;

import com.utp.proyecto.dto.CourseSummaryResponse;
import com.utp.proyecto.dto.CourseProgressResponse;
import com.utp.proyecto.dto.CourseResponse;
import com.utp.proyecto.dto.CreateCourseRequest;
import com.utp.proyecto.dto.CreateLessonRequest;
import com.utp.proyecto.dto.CreateModuleRequest;
import com.utp.proyecto.dto.LessonResponse;
import com.utp.proyecto.dto.ModuleResponse;
import com.utp.proyecto.dto.UpdateCourseRequest;
import com.utp.proyecto.dto.UpdateModuleRequest;
import com.utp.proyecto.dto.UpdateLessonRequest;
import com.utp.proyecto.exceptions.ApiException;
import com.utp.proyecto.models.AppUser;
import com.utp.proyecto.models.Category;
import com.utp.proyecto.models.Course;
import com.utp.proyecto.models.CourseLevel;
import com.utp.proyecto.models.CourseModule;
import com.utp.proyecto.models.CourseStatus;
import com.utp.proyecto.models.Enrollment;
import com.utp.proyecto.models.Lesson;
import com.utp.proyecto.models.LessonProgress;
import com.utp.proyecto.models.LessonType;
import com.utp.proyecto.repositories.AppUserRepository;
import com.utp.proyecto.repositories.CategoryRepository;
import com.utp.proyecto.repositories.CourseModuleRepository;
import com.utp.proyecto.repositories.CourseRepository;
import com.utp.proyecto.repositories.EnrollmentRepository;
import com.utp.proyecto.repositories.LessonProgressRepository;
import com.utp.proyecto.repositories.LessonRepository;
import com.utp.proyecto.security.CurrentUserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final LessonProgressRepository progressRepository;
    private final AppUserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CurrentUserService currentUserService;

    public CourseService(CourseRepository courseRepository, CourseModuleRepository moduleRepository,
                         LessonRepository lessonRepository, EnrollmentRepository enrollmentRepository,
                         LessonProgressRepository progressRepository, AppUserRepository userRepository,
                         CategoryRepository categoryRepository, CurrentUserService currentUserService) {
        this.courseRepository = courseRepository;
        this.moduleRepository = moduleRepository;
        this.lessonRepository = lessonRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.progressRepository = progressRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.currentUserService = currentUserService;
    }

    public List<CourseSummaryResponse> listPublishedCourses() {
        return courseRepository.findByStatus(CourseStatus.PUBLISHED).stream()
                .map(this::toSummary)
                .toList();
    }

    public CourseResponse getCourseDetail(Long courseId) {
        Course course = findCourse(courseId);
        Long userId = safeCurrentUserId();
        return toCourseResponse(course, userId);
    }

    public CourseResponse createCourse(CreateCourseRequest request) {
        Long userId = currentUserService.getCurrentUserId();
        AppUser instructor = findUser(userId);
        Course course = new Course();
        course.setInstructor(instructor);
        course.setTitle(request.title());
        course.setDescription(request.description());
        course.setPriceCredits(request.priceCredits());
        course.setCoverUrl(request.coverUrl());
        course.setLevel(CourseLevel.valueOf(request.level().toUpperCase()));
        course.setStatus(CourseStatus.DRAFT);
        course.setCertifiable(false);
        course.setCreatedAt(LocalDateTime.now());
        if (request.categoryId() != null) {
            course.setCategory(categoryRepository.findById(request.categoryId()).orElse(null));
        }
        return toCourseResponse(courseRepository.save(course), userId);
    }

    public CourseResponse updateCourse(Long courseId, UpdateCourseRequest request) {
        Long userId = currentUserService.getCurrentUserId();
        Course course = findCourse(courseId);
        if (!course.getInstructor().getId().equals(userId)) {
            throw ApiException.forbidden("Solo el instructor puede editar este curso.");
        }
        if (request.title() != null) course.setTitle(request.title());
        if (request.description() != null) course.setDescription(request.description());
        if (request.coverUrl() != null) course.setCoverUrl(request.coverUrl());
        if (request.priceCredits() >= 0) course.setPriceCredits(request.priceCredits());
        if (request.level() != null) course.setLevel(CourseLevel.valueOf(request.level().toUpperCase()));
        if (request.status() != null) course.setStatus(CourseStatus.valueOf(request.status().toUpperCase()));
        if (request.isCertifiable() != null) course.setCertifiable(request.isCertifiable());
        if (request.categoryId() != null) {
            course.setCategory(categoryRepository.findById(request.categoryId()).orElse(null));
        }
        return toCourseResponse(courseRepository.save(course), userId);
    }

    public List<CourseSummaryResponse> getMyCourses() {
        Long userId = currentUserService.getCurrentUserId();
        return courseRepository.findByInstructorId(userId).stream()
                .map(this::toSummary)
                .toList();
    }

    public List<CourseSummaryResponse> getMyEnrolledCourses() {
        Long userId = currentUserService.getCurrentUserId();
        return enrollmentRepository.findByStudentId(userId).stream()
                .map(e -> toSummary(e.getCourse()))
                .toList();
    }

    public ModuleResponse addModule(Long courseId, CreateModuleRequest request) {
        Long userId = currentUserService.getCurrentUserId();
        Course course = findCourse(courseId);
        if (!course.getInstructor().getId().equals(userId)) {
            throw ApiException.forbidden("Solo el instructor puede agregar módulos.");
        }
        CourseModule module = new CourseModule();
        module.setCourse(course);
        module.setTitle(request.title());
        module.setOrderIndex(request.orderIndex());
        CourseModule saved = moduleRepository.save(module);
        return new ModuleResponse(saved.getId(), saved.getTitle(), saved.getOrderIndex(), List.of());
    }

    public LessonResponse addLesson(Long moduleId, CreateLessonRequest request) {
        Long userId = currentUserService.getCurrentUserId();
        CourseModule module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> ApiException.notFound("Módulo no encontrado."));
        if (!module.getCourse().getInstructor().getId().equals(userId)) {
            throw ApiException.forbidden("Solo el instructor puede agregar lecciones.");
        }
        Lesson lesson = new Lesson();
        lesson.setModule(module);
        lesson.setTitle(request.title());
        lesson.setContent(request.content());
        lesson.setVideoUrl(request.videoUrl());
        lesson.setType(LessonType.valueOf(request.type().toUpperCase()));
        lesson.setOrderIndex(request.orderIndex());
        Lesson saved = lessonRepository.save(lesson);
        return new LessonResponse(saved.getId(), saved.getTitle(), saved.getContent(),
                saved.getVideoUrl(), saved.getType().name(), saved.getOrderIndex(), false);
    }

    public ModuleResponse updateModule(Long moduleId, UpdateModuleRequest request) {
        Long userId = currentUserService.getCurrentUserId();
        CourseModule module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> ApiException.notFound("Módulo no encontrado."));
        if (!module.getCourse().getInstructor().getId().equals(userId)) {
            throw ApiException.forbidden("Solo el instructor puede editar módulos.");
        }
        if (request.title() != null) module.setTitle(request.title());
        CourseModule saved = moduleRepository.save(module);
        List<LessonResponse> lessons = lessonRepository.findByModuleIdOrderByOrderIndex(saved.getId())
                .stream()
                .map(l -> new LessonResponse(l.getId(), l.getTitle(), l.getContent(),
                        l.getVideoUrl(), l.getType().name(), l.getOrderIndex(), false))
                .toList();
        return new ModuleResponse(saved.getId(), saved.getTitle(), saved.getOrderIndex(), lessons);
    }

    public void deleteModule(Long moduleId) {
        Long userId = currentUserService.getCurrentUserId();
        CourseModule module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> ApiException.notFound("Módulo no encontrado."));
        if (!module.getCourse().getInstructor().getId().equals(userId)) {
            throw ApiException.forbidden("Solo el instructor puede eliminar módulos.");
        }
        moduleRepository.delete(module);
    }

    public LessonResponse updateLesson(Long lessonId, UpdateLessonRequest request) {
        Long userId = currentUserService.getCurrentUserId();
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> ApiException.notFound("Lección no encontrada."));
        if (!lesson.getModule().getCourse().getInstructor().getId().equals(userId)) {
            throw ApiException.forbidden("Solo el instructor puede editar lecciones.");
        }
        if (request.title() != null) lesson.setTitle(request.title());
        if (request.content() != null) lesson.setContent(request.content());
        if (request.videoUrl() != null) lesson.setVideoUrl(request.videoUrl());
        if (request.type() != null) lesson.setType(LessonType.valueOf(request.type().toUpperCase()));
        Lesson saved = lessonRepository.save(lesson);
        return new LessonResponse(saved.getId(), saved.getTitle(), saved.getContent(),
                saved.getVideoUrl(), saved.getType().name(), saved.getOrderIndex(), false);
    }

    public void deleteLesson(Long lessonId) {
        Long userId = currentUserService.getCurrentUserId();
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> ApiException.notFound("Lección no encontrada."));
        if (!lesson.getModule().getCourse().getInstructor().getId().equals(userId)) {
            throw ApiException.forbidden("Solo el instructor puede eliminar lecciones.");
        }
        lessonRepository.delete(lesson);
    }

    public CourseResponse enrollCourse(Long courseId) {
        Long userId = currentUserService.getCurrentUserId();
        Course course = findCourse(courseId);
        if (!course.getStatus().equals(CourseStatus.PUBLISHED)) {
            throw ApiException.badRequest("El curso no está disponible.");
        }
        if (enrollmentRepository.findByStudentIdAndCourseId(userId, courseId).isPresent()) {
            throw ApiException.badRequest("Ya estás inscrito en este curso.");
        }
        AppUser student = findUser(userId);
        if (student.getCredits() < course.getPriceCredits()) {
            throw ApiException.badRequest("Créditos insuficientes para inscribirse.");
        }
        student.setCredits(student.getCredits() - course.getPriceCredits());

        int instructorShare = (int) Math.round(course.getPriceCredits() * 0.7);
        AppUser instructor = course.getInstructor();
        instructor.setCredits(instructor.getCredits() + instructorShare);
        userRepository.save(student);
        userRepository.save(instructor);

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrolledAt(LocalDateTime.now());
        enrollment.setCompleted(false);
        enrollmentRepository.save(enrollment);

        return toCourseResponse(course, userId);
    }

    public LessonResponse markLessonComplete(Long lessonId) {
        Long userId = currentUserService.getCurrentUserId();
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> ApiException.notFound("Lección no encontrada."));
        Long courseId = lesson.getModule().getCourse().getId();
        Course course = lesson.getModule().getCourse();
        boolean isInstructor = course.getInstructor().getId().equals(userId);
        boolean isEnrolled = enrollmentRepository.findByStudentIdAndCourseId(userId, courseId).isPresent();
        if (!isEnrolled && !isInstructor) {
            throw ApiException.forbidden("No estás inscrito en este curso.");
        }

        if (progressRepository.findByUserIdAndLessonId(userId, lessonId).isEmpty()) {
            AppUser user = findUser(userId);
            LessonProgress progress = new LessonProgress();
            progress.setUser(user);
            progress.setLesson(lesson);
            progress.setCompletedAt(LocalDateTime.now());
            progressRepository.save(progress);

            long totalLessons = countCourseLessons(courseId);
            long completedLessons = progressRepository.countCompletedLessonsForCourse(userId, courseId);
            if (totalLessons > 0 && completedLessons >= totalLessons) {
                enrollmentRepository.findByStudentIdAndCourseId(userId, courseId).ifPresent(enrollment -> {
                    enrollment.setCompleted(true);
                    enrollmentRepository.save(enrollment);
                });
            }
        }
        return new LessonResponse(lesson.getId(), lesson.getTitle(), lesson.getContent(),
                lesson.getVideoUrl(), lesson.getType().name(), lesson.getOrderIndex(), true);
    }

    public CourseProgressResponse getCourseProgress(Long courseId) {
        Long userId = currentUserService.getCurrentUserId();
        Course course = findCourse(courseId);
        boolean isInstructor = course.getInstructor().getId().equals(userId);
        boolean isEnrolled = enrollmentRepository.findByStudentIdAndCourseId(userId, courseId).isPresent();
        if (!isEnrolled && !isInstructor) {
            throw ApiException.forbidden("No estás inscrito en este curso.");
        }
        long total = countCourseLessons(courseId);
        long done = progressRepository.countCompletedLessonsForCourse(userId, courseId);
        int pct = total == 0 ? 0 : (int) Math.round((double) done / total * 100);
        boolean completed = total > 0 && done >= total;
        return new CourseProgressResponse(total, done, pct, completed);
    }

    private long countCourseLessons(Long courseId) {
        return moduleRepository.findByCourseIdOrderByOrderIndex(courseId).stream()
                .mapToLong(m -> lessonRepository.countByModuleId(m.getId()))
                .sum();
    }

    private CourseResponse toCourseResponse(Course course, Long currentUserId) {
        List<ModuleResponse> modules = buildModules(course.getId(), currentUserId);
        long lessonsCount = modules.stream().mapToLong(m -> m.lessons().size()).sum();
        boolean enrolled = currentUserId != null &&
                enrollmentRepository.findByStudentIdAndCourseId(currentUserId, course.getId()).isPresent();
        boolean isInstructor = currentUserId != null && course.getInstructor().getId().equals(currentUserId);
        int progress = 0;
        if ((enrolled || isInstructor) && currentUserId != null) {
            long total = lessonsCount;
            long done = progressRepository.countCompletedLessonsForCourse(currentUserId, course.getId());
            progress = total == 0 ? 0 : (int) Math.round((double) done / total * 100);
        }
        return new CourseResponse(
                course.getId(), course.getTitle(), course.getDescription(),
                course.getPriceCredits(), course.getCoverUrl(),
                course.getLevel().name(), course.getStatus().name(), course.isCertifiable(),
                course.getInstructor().getId(), course.getInstructor().getName(), course.getInstructor().getAvatarUrl(),
                course.getCategory() != null ? course.getCategory().getId() : null,
                course.getCategory() != null ? course.getCategory().getName() : null,
                modules.size(), lessonsCount,
                enrollmentRepository.countByCourseId(course.getId()),
                course.getCreatedAt(), enrolled, progress, modules
        );
    }

    private List<ModuleResponse> buildModules(Long courseId, Long currentUserId) {
        Set<Long> completedLessons = currentUserId == null ? Set.of() :
                progressRepository.findByUserId(currentUserId).stream()
                        .map(lp -> lp.getLesson().getId())
                        .collect(Collectors.toSet());
        return moduleRepository.findByCourseIdOrderByOrderIndex(courseId).stream()
                .map(m -> {
                    List<LessonResponse> lessons = lessonRepository.findByModuleIdOrderByOrderIndex(m.getId())
                            .stream()
                            .map(l -> new LessonResponse(l.getId(), l.getTitle(), l.getContent(),
                                    l.getVideoUrl(), l.getType().name(), l.getOrderIndex(),
                                    completedLessons.contains(l.getId())))
                            .toList();
                    return new ModuleResponse(m.getId(), m.getTitle(), m.getOrderIndex(), lessons);
                })
                .toList();
    }

    private CourseSummaryResponse toSummary(Course course) {
        long lessonsCount = moduleRepository.findByCourseIdOrderByOrderIndex(course.getId()).stream()
                .mapToLong(m -> lessonRepository.countByModuleId(m.getId()))
                .sum();
        return new CourseSummaryResponse(
                course.getId(), course.getTitle(), course.getDescription(),
                course.getPriceCredits(), course.getCoverUrl(),
                course.getLevel().name(), course.getStatus().name(), course.isCertifiable(),
                course.getInstructor().getId(), course.getInstructor().getName(), course.getInstructor().getAvatarUrl(),
                course.getCategory() != null ? course.getCategory().getId() : null,
                course.getCategory() != null ? course.getCategory().getName() : null,
                lessonsCount, enrollmentRepository.countByCourseId(course.getId()),
                course.getCreatedAt()
        );
    }

    private Course findCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Curso no encontrado."));
    }

    private AppUser findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Usuario no encontrado."));
    }

    private Long safeCurrentUserId() {
        return currentUserService.getCurrentUserIdOrNull();
    }
}
