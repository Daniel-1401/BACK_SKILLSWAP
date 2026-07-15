package com.utp.proyecto.controllers;

import com.utp.proyecto.dto.AddUserSkillRequest;
import com.utp.proyecto.dto.PagedResponse;
import com.utp.proyecto.dto.ReviewResponse;
import com.utp.proyecto.dto.UpdateProfileRequest;
import com.utp.proyecto.dto.UpdateUserSkillRequest;
import com.utp.proyecto.dto.UserProfileResponse;
import com.utp.proyecto.dto.UserSkillMutationResponse;
import com.utp.proyecto.dto.UserSkillResponse;
import com.utp.proyecto.models.UserSkillType;
import com.utp.proyecto.services.ReviewService;
import com.utp.proyecto.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final ReviewService reviewService;

    public UserController(UserService userService, ReviewService reviewService) {
        this.userService = userService;
        this.reviewService = reviewService;
    }

    @GetMapping
    public PagedResponse<UserProfileResponse> listUsers(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String mode,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return userService.listUsers(q, categoryId, mode, page, pageSize);
    }

    @GetMapping("/me/profile")
    public UserProfileResponse getCurrentProfile() {
        return userService.getCurrentProfile();
    }

    @PutMapping("/me/profile")
    public UserProfileResponse updateCurrentProfile(@RequestBody UpdateProfileRequest request) {
        return userService.updateCurrentProfile(request);
    }

    @PostMapping("/me/skills/teaches")
    public ResponseEntity<UserSkillMutationResponse> addTeachesSkill(@RequestBody AddUserSkillRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addSkillToCurrentUser(request, UserSkillType.TEACHES));
    }

    @PostMapping("/me/skills/wants")
    public ResponseEntity<UserSkillMutationResponse> addWantsSkill(@RequestBody AddUserSkillRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addSkillToCurrentUser(request, UserSkillType.WANTS));
    }

    @PutMapping("/me/skills/{id}")
    public UserSkillMutationResponse updateSkill(@PathVariable Long id, @RequestBody UpdateUserSkillRequest request) {
        return userService.updateSkillForCurrentUser(id, request);
    }

    @DeleteMapping("/me/skills/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id) {
        userService.deleteSkillFromCurrentUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public UserProfileResponse getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @GetMapping("/{id}/skills")
    public Map<String, List<UserSkillResponse>> getUserSkills(@PathVariable Long id) {
        return userService.getUserSkills(id);
    }

    @GetMapping("/{id}/reviews")
    public List<ReviewResponse> getUserReviews(@PathVariable Long id) {
        return reviewService.getUserReviews(id);
    }
}
