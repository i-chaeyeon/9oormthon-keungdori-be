package goormthonuniv.kengdori.backend.service;

import goormthonuniv.kengdori.backend.DTO.*;
import goormthonuniv.kengdori.backend.domain.Place;
import goormthonuniv.kengdori.backend.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {

    // C
    ReviewResponseDTO createReview(User user, ReviewRequestDTO reviewRequestDTO);

    // R
    Page<VisitedPlaceResponseDTO> searchMyReviewedPlaces(User user, String keyword, Pageable pageable);
    ReviewListByPlaceDTO findMyReviewsByPlace(String googleId, User user, Pageable pageable);

    // U
    ReviewResponseDTO updateReview(User user, Long reviewId, ReviewUpdateRequestDTO reviewUpdateRequestDTO);
    // D

    void deleteReview(User user, Long reviewId);

    Page<VisitedPlaceResponseDTO> findPlacesByHashtag(String hashtag, Pageable pageable);
}
