package goormthonuniv.kengdori.backend.service.review;

import goormthonuniv.kengdori.backend.domain.user.User;
import goormthonuniv.kengdori.backend.dto.place.VisitedPlaceResponseDTO;
import goormthonuniv.kengdori.backend.dto.review.ReviewDetailsResponseDTO;
import goormthonuniv.kengdori.backend.dto.review.ReviewListByPlaceDTO;
import goormthonuniv.kengdori.backend.dto.review.ReviewRequestDTO;
import goormthonuniv.kengdori.backend.dto.review.ReviewResponseDTO;
import goormthonuniv.kengdori.backend.dto.review.ReviewUpdateRequestDTO;
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
