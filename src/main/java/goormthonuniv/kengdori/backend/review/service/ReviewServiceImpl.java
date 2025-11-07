package goormthonuniv.kengdori.backend.review.service;

import goormthonuniv.kengdori.backend.global.exception.UnauthorizedException;
import goormthonuniv.kengdori.backend.domain.PlaceHashtag;
import goormthonuniv.kengdori.backend.domain.UserHashtag;
import goormthonuniv.kengdori.backend.hashtag.repository.PlaceHashtagRepository;
import goormthonuniv.kengdori.backend.place.DTO.VisitedPlaceResponseDTO;
import goormthonuniv.kengdori.backend.domain.Place;
import goormthonuniv.kengdori.backend.place.repository.PlaceRepository;
import goormthonuniv.kengdori.backend.review.DTO.*;
import goormthonuniv.kengdori.backend.domain.Review;
import goormthonuniv.kengdori.backend.review.repository.ReviewRepository;
import goormthonuniv.kengdori.backend.hashtag.repository.UserHashtagRepository;
import goormthonuniv.kengdori.backend.hashtag.service.HashtagService;
import goormthonuniv.kengdori.backend.place.service.PlaceService;
import goormthonuniv.kengdori.backend.domain.User;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService{

    private final PlaceService placeService;
    private final HashtagService hashtagService;
    private final PlaceRepository placeRepository;
    private final ReviewRepository reviewRepository;
    private final PlaceHashtagRepository placeHashtagRepository;
    private final UserHashtagRepository userHashtagRepository;

    @Override
    @Transactional
    public ReviewResponseDTO createReview(User user, ReviewRequestDTO dto) {
        log.info("리뷰 생성 시도 - userId: {}, placeName: {}", user.getId(), dto.getPlaceName());

        Place place = placeService.findOrCreatePlace(dto.toPlace());

        log.info("생성된 장소 - userId: {}, place: {}", user.getId(), place.getName());
        log.info("리뷰 생성 시도 - userId: {}, maintag {}", user.getId(), dto.getMainTag());

        UserHashtag mainTag = hashtagService.findUserHashtag(user, dto.getMainTag());
        PlaceHashtag mainRel = PlaceHashtag.builder()
                .place(place)
                .userHashtag(mainTag)
                .isMain(true)
                .build();
        placeHashtagRepository.save(mainRel);

        List<PlaceHashtag> subRelList = dto.getSubTags().stream()
                .map(tag -> PlaceHashtag.builder()
                        .place(place)
                        .userHashtag(hashtagService.findUserHashtag(user, tag))
                        .isMain(false)
                        .build())
                .toList();
        placeHashtagRepository.saveAll(subRelList);

        Review review = Review.builder()
                .user(user)
                .place(place)
                .rating(dto.getRating())
                .imageUrl(dto.getImageUrl())
                .memo(dto.getMemo())
                .build();
        reviewRepository.save(review);

        log.info("리뷰 생성 완료 - reviewId: {}, placeId: {}", review.getId(), place.getId());

        return new ReviewResponseDTO(
                place.getId(),
                review.getId(),
                place.getName(),
                review.getRating(),
                mainTag.getHashtag(),
                dto.getSubTags(),
                review.getImageUrl(),
                review.getMemo(),
                review.getCreatedAt()
        );
    }

    @Override
    @Transactional
    public ReviewResponseDTO updateReview(User user, Long reviewId, ReviewUpdateRequestDTO dto) {

        log.info("리뷰 수정 시도 - reviewId: {}, userId: {}", reviewId, user.getId());

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));

        review.setRating(dto.getRating());
        review.setImageUrl(dto.getImageUrl());
        review.setMemo(dto.getMemo());

        Place place = review.getPlace();
        // 전부 지우고 다시 설정
        placeHashtagRepository.deleteByPlaceAndUserHashtag_User(place, user);

        List<String> allTags = new ArrayList<>(dto.getSubTags());
        allTags.add(dto.getMainTag());

        List<UserHashtag> foundHashtags = userHashtagRepository.findByUserAndHashtagIn(user, allTags);
        if (foundHashtags.size() != allTags.stream().distinct().count()) {
            throw new EntityNotFoundException("존재하지 않는 해시태그가 포함되어 있습니다.");
        }
        Map<String, UserHashtag> hashtagMap = foundHashtags.stream()
                .collect(Collectors.toMap(UserHashtag::getHashtag, tag -> tag));

        UserHashtag mainTag = hashtagMap.get(dto.getMainTag());
        PlaceHashtag mainRel = PlaceHashtag.builder()
                .place(place)
                .userHashtag(mainTag)
                .isMain(true)
                .build();

        List<PlaceHashtag> subRelList = dto.getSubTags().stream()
                .map(tagStr -> PlaceHashtag.builder()
                        .place(place)
                        .userHashtag(hashtagMap.get(tagStr))
                        .isMain(false)
                        .build())
                .toList();

        List<PlaceHashtag> allPlaceHashtags = new ArrayList<>(subRelList);
        allPlaceHashtags.add(mainRel);
        placeHashtagRepository.saveAll(allPlaceHashtags);

        log.info("리뷰 수정 완료 - reviewId: {}", reviewId);
        return new ReviewResponseDTO(
                place.getId(),
                review.getId(),
                place.getName(),
                review.getRating(),
                mainTag.getHashtag(),
                dto.getSubTags(),
                review.getImageUrl(),
                review.getMemo(),
                review.getCreatedAt()
        );
    }

    @Override
    @Transactional
    public void deleteReview(User user, Long reviewId) {

        log.info("리뷰 삭제 시도 - reviewId: {}, userId: {}", reviewId, user.getId());
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));
        Place place = review.getPlace();

        // 사용자가 쓴 리뷰인지 확인해줘야되려나

        log.info("리뷰에 연결된 해시태그 삭제 - placeId: {}, userId: {}", place.getId(), user.getId());
        placeHashtagRepository.deleteByPlaceAndUserHashtag_User(place, user);

        log.info("리뷰 엔티티 삭제 - reviewId: {}", reviewId);
        reviewRepository.delete(review);

        log.info("리뷰 삭제 완료 - reviewId: {}", reviewId);
    }

    @Override
    public Page<VisitedPlaceResponseDTO> searchMyReviewedPlaces(User user, String keyword, Pageable pageable) {
        log.info("리뷰 장소 검색 시도 - userId: {}, keyword: {}", user.getId(), keyword);

        Page<Place> places = placeRepository.findPlacesByUserReviewAndKeyword(user, keyword, pageable);

        log.info("검색 완료 - 장소 총 {}개", places.getTotalElements());

        return places.map(VisitedPlaceResponseDTO::new);
    }

    @Override
    public Page<VisitedPlaceResponseDTO> findPlacesByHashtag(String hashtag, Pageable pageable) {

        log.info("해시태그 검색 - hashtag: {}", hashtag);

        Page<Place> places = placeRepository.findByHashtag(hashtag, pageable);

        log.info("해시태그 검색 완료 - 장소 총 {}개", places.getTotalElements());

        return places.map(VisitedPlaceResponseDTO::new);
    }

    @Override
    public ReviewListByPlaceDTO findMyReviewsByPlace(String googleId, User user, Pageable pageable){

        Optional<Place> optionalPlace = placeRepository.findBygoogleId(googleId);

        if (optionalPlace.isEmpty()) {
            log.info("요청한 장소를 찾을 수 없습니다. googleId: {}. 빈 리뷰 목록을 반환합니다.", googleId);
            return new ReviewListByPlaceDTO();
        }

        Place place = optionalPlace.get();
        log.info("장소의 리뷰 목록 불러오기 - 장소명: {}", place.getName());

        Page<Review> reviewPage = reviewRepository.findByPlaceAndUser(place, user, pageable);
        log.info("장소의 리뷰 목록 조회 완료 - 리뷰 총 {}개", reviewPage.getTotalElements());

        return new ReviewListByPlaceDTO(place, reviewPage);
    }

    @Override
    public ReviewDetailsResponseDTO getReviewDetailsForUser(Long reviewId, User user) {

        log.info("특정 리뷰의 상세보기 시작 - 리뷰 아이디: {}", reviewId);

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("해당 리뷰 없음. 아이디: " + reviewId));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("리뷰를 조회할 권한이 없습니다.");
        }

        return new ReviewDetailsResponseDTO(review);
    }

    public boolean hasReviewWithin2Weeks(Long userId) {
        LocalDateTime twoWeeksAgo = LocalDateTime.now().minusWeeks(2);
        return reviewRepository.existsByUserIdAndCreatedAtAfter(userId, twoWeeksAgo);
    }
}
