package goormthonuniv.kengdori.backend.review.service;

import goormthonuniv.kengdori.backend.user.domain.User;
import goormthonuniv.kengdori.backend.place.DTO.VisitedPlaceResponseDTO;
import goormthonuniv.kengdori.backend.review.DTO.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {

    // C
    ReviewResponseDTO createReview(User user, ReviewRequestDTO reviewRequestDTO);

    // R
    Page<VisitedPlaceResponseDTO> searchMyReviewedPlaces(User user, String keyword, Pageable pageable);
    ReviewListByPlaceDTO findMyReviewsByPlace(String googleId, User user, Pageable pageable);
    ReviewDetailsResponseDTO getReviewDetailsForUser(Long reviewId, User user);

    // U
    ReviewResponseDTO updateReview(User user, Long reviewId, ReviewUpdateRequestDTO reviewUpdateRequestDTO);
    // D

    void deleteReview(User user, Long reviewId);

    Page<VisitedPlaceResponseDTO> findPlacesByHashtag(String hashtag, Pageable pageable);
}
