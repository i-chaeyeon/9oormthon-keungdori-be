package goormthonuniv.kengdori.backend.dto.review;

import goormthonuniv.kengdori.backend.domain.place.PlaceMainTag;
import goormthonuniv.kengdori.backend.domain.review.Review;
import goormthonuniv.kengdori.backend.domain.review.ReviewHashtag;
import goormthonuniv.kengdori.backend.dto.hashtag.UserHashtagDTO;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ReviewDetailsResponseDTO {
    private final Long reviewId;
    private final Long placeId;
    private final String placeName;
    private final Double rating;
    private final String memo;
    private final String imageUrl;
    private final LocalDateTime createdAt;

    private final UserHashtagDTO mainTag;
    private final List<UserHashtagDTO> subTags;

    public ReviewDetailsResponseDTO(Review review, PlaceMainTag mainTag, List<ReviewHashtag> subTags) {
        this.reviewId = review.getId();
        this.placeId = review.getPlace().getId();
        this.placeName = review.getPlace().getName();
        this.rating = review.getRating();
        this.memo = review.getMemo();
        this.imageUrl = review.getImageUrl();
        this.createdAt = review.getCreatedAt();

        this.mainTag = (mainTag != null)
                ? new UserHashtagDTO(mainTag.getUserHashtag())
                : null;

        this.subTags = subTags.stream()
                .map(rh -> new UserHashtagDTO(rh.getUserHashtag()))
                .toList();
    }
}
