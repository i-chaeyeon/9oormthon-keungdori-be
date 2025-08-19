package goormthonuniv.kengdori.backend.service;

import goormthonuniv.kengdori.backend.DTO.ReviewRequestDTO;
import goormthonuniv.kengdori.backend.DTO.ReviewResponseDTO;
import goormthonuniv.kengdori.backend.DTO.ReviewUpdateRequestDTO;
import goormthonuniv.kengdori.backend.domain.User;

public interface ReviewService {

    // C
    ReviewResponseDTO createReview(User user, ReviewRequestDTO reviewRequestDTO);

    // R

    // U
    ReviewResponseDTO updateReview(User user, Long reviewId, ReviewUpdateRequestDTO reviewUpdateRequestDTO);
    // D
}
