package goormthonuniv.kengdori.backend.service;

import goormthonuniv.kengdori.backend.DTO.ReviewRequestDTO;
import goormthonuniv.kengdori.backend.DTO.ReviewResponseDTO;
import goormthonuniv.kengdori.backend.DTO.ReviewUpdateRequestDTO;
import goormthonuniv.kengdori.backend.domain.*;
import goormthonuniv.kengdori.backend.repository.PlaceHashtagRepository;
import goormthonuniv.kengdori.backend.repository.ReviewRepository;
import goormthonuniv.kengdori.backend.repository.UserHashtagRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService{

    private final PlaceService placeService;
    private final HashtagService hashtagService;
    private final ReviewRepository reviewRepository;
    private final PlaceHashtagRepository placeHashtagRepository;
    private final UserHashtagRepository userHashtagRepository;


    @Override
    public ReviewResponseDTO createReview(User user, ReviewRequestDTO dto) {

        Place place = placeService.findOrCreatePlace(dto.toPlace());

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

        return new ReviewResponseDTO(
                place.getId(),
                review.getId(),
                place.getName(),
                review.getRating(),
                mainTag.getHashtag(),
                dto.getSubTags(),
                review.getImageUrl(),
                review.getMemo()
        );
    }

    @Override
    @Transactional
    public ReviewResponseDTO updateReview(User user, Long reviewId, ReviewUpdateRequestDTO dto) {

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

        return new ReviewResponseDTO(
                place.getId(),
                review.getId(),
                place.getName(),
                review.getRating(),
                mainTag.getHashtag(),
                dto.getSubTags(),
                review.getImageUrl(),
                review.getMemo()
        );
    }
}
