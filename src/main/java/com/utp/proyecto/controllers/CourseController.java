package com.utp.proyecto.controllers;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import com.utp.proyecto.services.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
@Tag(name = "Courses", description = "Marketplace de cursos — Pilar 1")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    @Operation(summary = "Listar cursos publicados")
    public List<CourseSummaryResponse> listCourses() {
        return courseService.listPublishedCourses();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detalle de curso con módulos y lecciones")
    public CourseResponse getCourse(@PathVariable Long id) {
        return courseService.getCourseDetail(id);
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo curso (instructor)")
    public ResponseEntity<CourseResponse> createCourse(@RequestBody CreateCourseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.createCourse(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar curso propio")
    public CourseResponse updateCourse(@PathVariable Long id, @RequestBody UpdateCourseRequest request) {
        return courseService.updateCourse(id, request);
    }

    @GetMapping("/me/teaching")
    @Operation(summary = "Cursos que estoy enseñando")
    public List<CourseSummaryResponse> myTeachingCourses() {
        return courseService.getMyCourses();
    }

    @GetMapping("/me/enrolled")
    @Operation(summary = "Cursos en los que estoy inscrito")
    public List<CourseSummaryResponse> myEnrolledCourses() {
        return courseService.getMyEnrolledCourses();
    }

    @PostMapping("/{id}/enroll")
    @Operation(summary = "Inscribirse en un curso (descuenta créditos)")
    public CourseResponse enrollCourse(@PathVariable Long id) {
        return courseService.enrollCourse(id);
    }

    @GetMapping("/{id}/progress")
    @Operation(summary = "Progreso del alumno en el curso")
    public CourseProgressResponse getCourseProgress(@PathVariable Long id) {
        return courseService.getCourseProgress(id);
    }

    @PostMapping("/modules/{moduleId}/lessons")
    @Operation(summary = "Agregar lección a un módulo")
    public ResponseEntity<LessonResponse> addLesson(@PathVariable Long moduleId, @RequestBody CreateLessonRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.addLesson(moduleId, request));
    }

    @PostMapping("/{id}/modules")
    @Operation(summary = "Agregar módulo al curso")
    public ResponseEntity<ModuleResponse> addModule(@PathVariable Long id, @RequestBody CreateModuleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.addModule(id, request));
    }

    @PutMapping("/modules/{moduleId}")
    @Operation(summary = "Actualizar título de un módulo")
    public ModuleResponse updateModule(@PathVariable Long moduleId, @RequestBody UpdateModuleRequest request) {
        return courseService.updateModule(moduleId, request);
    }

    @DeleteMapping("/modules/{moduleId}")
    @Operation(summary = "Eliminar módulo y sus lecciones")
    public ResponseEntity<Void> deleteModule(@PathVariable Long moduleId) {
        courseService.deleteModule(moduleId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/lessons/{lessonId}")
    @Operation(summary = "Actualizar una lección")
    public LessonResponse updateLesson(@PathVariable Long lessonId, @RequestBody UpdateLessonRequest request) {
        return courseService.updateLesson(lessonId, request);
    }

    @DeleteMapping("/lessons/{lessonId}")
    @Operation(summary = "Eliminar una lección")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long lessonId) {
        courseService.deleteLesson(lessonId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/lessons/{lessonId}/complete")
    @Operation(summary = "Marcar lección como completada")
    public LessonResponse markLessonComplete(@PathVariable Long lessonId) {
        return courseService.markLessonComplete(lessonId);
    }
}
