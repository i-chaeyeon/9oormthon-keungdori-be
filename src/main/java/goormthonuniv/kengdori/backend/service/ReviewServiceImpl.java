package goormthonuniv.kengdori.backend.service;

import goormthonuniv.kengdori.backend.DTO.ReviewRequestDTO;
import goormthonuniv.kengdori.backend.DTO.ReviewResponseDTO;
import goormthonuniv.kengdori.backend.domain.*;
import goormthonuniv.kengdori.backend.repository.PlaceHashtagRepository;
import goormthonuniv.kengdori.backend.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService{

    private final PlaceService placeService;
    private final HashtagService hashtagService;
    private final ReviewRepository reviewRepository;
    private final PlaceHashtagRepository placeHashtagRepository;


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
}
