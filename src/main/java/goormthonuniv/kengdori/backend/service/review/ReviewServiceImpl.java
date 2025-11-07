package goormthonuniv.kengdori.backend.service.review;

import goormthonuniv.kengdori.backend.domain.place.PlaceMainTag;
import goormthonuniv.kengdori.backend.domain.place.PlaceMainTagRepository;
import goormthonuniv.kengdori.backend.domain.review.ReviewHashtag;
import goormthonuniv.kengdori.backend.domain.review.ReviewHashtagRepository;
import goormthonuniv.kengdori.backend.dto.review.ReviewDetailsResponseDTO;
import goormthonuniv.kengdori.backend.dto.review.ReviewListByPlaceDTO;
import goormthonuniv.kengdori.backend.dto.review.ReviewListResponseDTO;
import goormthonuniv.kengdori.backend.dto.review.ReviewRequestDTO;
import goormthonuniv.kengdori.backend.dto.review.ReviewResponseDTO;
import goormthonuniv.kengdori.backend.dto.review.ReviewUpdateRequestDTO;
import goormthonuniv.kengdori.backend.global.DTO.PageInfoDTO;
import goormthonuniv.kengdori.backend.global.exception.UnauthorizedException;
import goormthonuniv.kengdori.backend.domain.hashtag.UserHashtag;
import goormthonuniv.kengdori.backend.dto.place.VisitedPlaceResponseDTO;
import goormthonuniv.kengdori.backend.domain.place.Place;
import goormthonuniv.kengdori.backend.domain.place.PlaceRepository;
import goormthonuniv.kengdori.backend.domain.review.Review;
import goormthonuniv.kengdori.backend.domain.review.ReviewRepository;
import goormthonuniv.kengdori.backend.domain.hashtag.UserHashtagRepository;
import goormthonuniv.kengdori.backend.service.hashtag.UserHashtagService;
import goormthonuniv.kengdori.backend.domain.user.User;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService{

    private final PlaceRepository placeRepository;
    private final ReviewRepository reviewRepository;
    private final UserHashtagRepository userHashtagRepository;
    private final ReviewHashtagRepository reviewHashtagRepository;
    private final PlaceMainTagRepository placeMainTagRepository;
    private final UserHashtagService userHashtagService;

    @Override
    @Transactional
    public ReviewResponseDTO createReview(User user, ReviewRequestDTO dto) {

        log.info("리뷰 생성 시도 - userId: {}, place: {}", user.getId(), dto.getPlaceName());

        Place place = placeRepository.findByGoogleId(dto.getGoogleId())
                .orElseGet(() -> placeRepository.save(dto.toPlace()));

        Review review = Review.builder()
                .user(user)
                .place(place)
                .rating(dto.getRating())
                .memo(dto.getMemo())
                .imageUrl(dto.getImageUrl())
                .build();
        reviewRepository.save(review);

        log.info("dto subtag {}, dto maintag: {}", dto.getSubTags(), dto.getMainTag());

        if (dto.getSubTags() != null) {
            for (String tagName : dto.getSubTags()) {
                UserHashtag userTag = userHashtagRepository
                        .findByUserAndHashtag(user, tagName)
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 해시태그: " + tagName));

                ReviewHashtag reviewHashtag = ReviewHashtag.builder()
                        .review(review)
                        .user(user)
                        .userHashtag(userTag)
                        .isMain(false)
                        .build();
                reviewHashtagRepository.save(reviewHashtag);
                review.getHashtags().add(reviewHashtag);
            }
        }

        if (dto.getMainTag() != null) {
            UserHashtag mainTag = userHashtagRepository
                    .findByUserAndHashtag(user, dto.getMainTag())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메인태그"));

            // 기존 메인태그 있으면 삭제
            placeMainTagRepository.deleteByPlaceAndUser(place, user);

            placeMainTagRepository.save(
                    PlaceMainTag.builder()
                            .place(place)
                            .user(user)
                            .userHashtag(mainTag)
                            .build()
            );
        }

        log.info("리뷰 생성 완료 - reviewId: {}", review.getId());

        return toReviewResponseDTO(review, user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VisitedPlaceResponseDTO> searchMyReviewedPlaces(
            User user,
            String keyword,
            Pageable pageable
    ) {

        Page<Place> places = placeRepository.findPlacesByUserReviewAndKeyword(user, keyword, pageable);

        return places.map(VisitedPlaceResponseDTO::new);
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewListByPlaceDTO findMyReviewsByPlace(String googleId, User user, Pageable pageable) {

        Place place = placeRepository.findByGoogleId(googleId).orElse(null);

        if (place == null) {
            return new ReviewListByPlaceDTO(
                    null,
                    List.of(),
                    new PageInfoDTO(Page.empty(pageable))
            );
        }

        Page<Review> reviewPage = reviewRepository.findByPlaceAndUser(place, user, pageable);

        List<ReviewListResponseDTO> reviewDTOs = reviewPage.getContent().stream()
                .map(review -> toReviewListResponseDTO(review, user))
                .toList();

        return new ReviewListByPlaceDTO(place, reviewDTOs, new PageInfoDTO(reviewPage));
    }

    private ReviewListResponseDTO toReviewListResponseDTO(Review review, User user) {

        String mainTag = placeMainTagRepository.findByPlaceAndUser(review.getPlace(), user)
                .map(pmt -> pmt.getUserHashtag().getHashtag())
                .orElse(null);

        List<String> subTags = reviewHashtagRepository.findByReviewAndUser(review, user).stream()
                .map(rh -> rh.getUserHashtag().getHashtag())
                .filter(tag -> !tag.equals(mainTag))  // 메인태그는 제외
                .toList();

        return new ReviewListResponseDTO(
                review.getId(),
                review.getRating(),
                review.getMemo(),
                mainTag,
                subTags,
                review.getImageUrl()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewDetailsResponseDTO getReviewDetailsForUser(Long reviewId, User user) {

        // 리뷰 존재 여부
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));

        Place place = review.getPlace();

        PlaceMainTag mainTag = placeMainTagRepository
                .findByPlaceAndUser(place, user)
                .orElse(null);

        List<ReviewHashtag> subTags = reviewHashtagRepository
                .findByReviewAndUser(review, user);

        // 4. DTO로 변환해서 반환
        return new ReviewDetailsResponseDTO(review, mainTag, subTags);
    }

    @Override
    @Transactional
    public ReviewResponseDTO updateReview(User user, Long reviewId, ReviewUpdateRequestDTO dto) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));


        review.setRating(dto.getRating());
        review.setMemo(dto.getMemo());
        review.setImageUrl(dto.getImageUrl());

        Place place = review.getPlace();

        if (dto.getMainTag() != null) {
            placeMainTagRepository.deleteByPlaceAndUser(place, user);

            if (!dto.getMainTag().isBlank()) {
                UserHashtag main = userHashtagService.findOrCreate(user, dto.getMainTag());
                placeMainTagRepository.save(
                        PlaceMainTag.builder()
                                .place(place)
                                .user(user)
                                .userHashtag(main)
                                .build()
                );
            }
        }

        reviewHashtagRepository.deleteByReviewAndUser(review, user);

        if (dto.getSubTags() != null) {
            for (String tag : dto.getSubTags()) {
                UserHashtag sub = userHashtagService.findOrCreate(user, tag);
                reviewHashtagRepository.save(
                        ReviewHashtag.builder()
                                .review(review)
                                .user(user)
                                .userHashtag(sub)
                                .isMain(false)
                                .build()
                );
            }
        }

        return toReviewResponseDTO(review, user);
    }

    private ReviewResponseDTO toReviewResponseDTO(Review review, User user) {
        Place place = review.getPlace();

        String mainTag = placeMainTagRepository.findByPlaceAndUser(place, user)
                .map(pmt -> pmt.getUserHashtag().getHashtag())
                .orElse(null);

        List<String> subTags = review.getHashtags().stream()
                .filter(rh -> Boolean.FALSE.equals(rh.getIsMain()))
                .map(rh -> rh.getUserHashtag().getHashtag())
                .toList();

        return new ReviewResponseDTO(
                place.getId(),
                review.getId(),
                place.getName(),
                review.getRating(),
                mainTag,
                subTags,
                review.getImageUrl(),
                review.getMemo(),
                review.getCreatedAt()
        );
    }

    @Override
    @Transactional
    public void deleteReview(User user, Long reviewId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 리뷰입니다. id=" + reviewId));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("본인의 리뷰만 삭제할 수 있습니다.");
        }

        reviewRepository.delete(review);

        log.info("리뷰 삭제 완료 - reviewId: {}, userId: {}", reviewId, user.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VisitedPlaceResponseDTO> findPlacesByHashtag(String hashtag, Pageable pageable) {
        Page<Place> placePage = placeRepository.findPlacesByHashtag(hashtag, pageable);
        return placePage.map(VisitedPlaceResponseDTO::new);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasReviewWithin2Weeks(Long userId) {

        LocalDateTime twoWeeksAgo = LocalDateTime.now().minusWeeks(2);

        return reviewRepository.existsByUserIdAndCreatedAtAfter(userId, twoWeeksAgo);
    }

}
