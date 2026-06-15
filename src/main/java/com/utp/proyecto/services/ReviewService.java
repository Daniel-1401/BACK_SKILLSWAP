package com.utp.proyecto.services;

import com.utp.proyecto.dto.ReviewRequest;
import com.utp.proyecto.dto.ReviewResponse;
import com.utp.proyecto.exceptions.ApiException;
import com.utp.proyecto.models.AppUser;
import com.utp.proyecto.models.LearningSession;
import com.utp.proyecto.models.Review;
import com.utp.proyecto.repositories.ReviewRepository;
import com.utp.proyecto.security.CurrentUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final SessionService sessionService;
    private final CurrentUserService currentUserService;

    public ReviewService(
            ReviewRepository reviewRepository,
            SessionService sessionService,
            CurrentUserService currentUserService
    ) {
        this.reviewRepository = reviewRepository;
        this.sessionService = sessionService;
        this.currentUserService = currentUserService;
    }

    public ReviewResponse create(Long sessionId, ReviewRequest request) {
        if (request.rating() < 1 || request.rating() > 5) {
            throw ApiException.badRequest("rating debe estar entre 1 y 5.");
        }

        LearningSession session = sessionService.findSessionForCurrentUser(sessionId);
        AppUser reviewer = currentUserService.getCurrentUser();
        AppUser reviewedUser = session.getTeacher().getId().equals(reviewer.getId())
                ? session.getLearner()
                : session.getTeacher();

        Review review = new Review();
        review.setSession(session);
        review.setReviewer(reviewer);
        review.setReviewedUser(reviewedUser);
        review.setRating(request.rating());
        review.setComment(request.comment());

        return toResponse(reviewRepository.save(review));
    }

    public List<ReviewResponse> getUserReviews(Long userId) {
        return reviewRepository.findByReviewedUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    private ReviewResponse toResponse(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getSession().getId(),
                review.getReviewer().getName(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt().toString()
        );
    }
}
