package goormthonuniv.kengdori.backend.service.review;

import goormthonuniv.kengdori.backend.domain.user.User;
import goormthonuniv.kengdori.backend.dto.place.VisitedPlaceResponseDTO;
import goormthonuniv.kengdori.backend.dto.review.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {

    // 리뷰 생성
    ReviewResponseDTO createReview(User user, ReviewRequestDTO reviewRequestDTO);

    // 방문했던 장소 검색
    Page<VisitedPlaceResponseDTO> searchMyReviewedPlaces(User user, String keyword, Pageable pageable);

    // 특정 장소에서 내가 남긴 리뷰 목록 조회
    ReviewListByPlaceDTO findMyReviewsByPlace(String googleId, User user, Pageable pageable);

    // 리뷰 상세 조회
    ReviewDetailsResponseDTO getReviewDetailsForUser(Long reviewId, User user);

    // 리뷰 수정
    ReviewResponseDTO updateReview(User user, Long reviewId, ReviewUpdateRequestDTO reviewUpdateRequestDTO);

    // 리뷰 삭제
    void deleteReview(User user, Long reviewId);

    // 2주 내 리뷰 작성 여부
    boolean hasReviewWithin2Weeks(Long userId);

    Page<VisitedPlaceResponseDTO> findPlacesByHashtag(String hashtag, Pageable pageable);
}
