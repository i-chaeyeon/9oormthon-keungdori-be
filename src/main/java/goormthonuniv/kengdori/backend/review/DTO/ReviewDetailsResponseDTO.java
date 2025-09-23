package goormthonuniv.kengdori.backend.review.DTO;

import goormthonuniv.kengdori.backend.place.domain.Place;
import goormthonuniv.kengdori.backend.hashtag.domain.PlaceHashtag;
import goormthonuniv.kengdori.backend.review.domain.Review;
import goormthonuniv.kengdori.backend.hashtag.DTO.HashtagInfoDTO;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ReviewDetailsResponseDTO {

    private final Long placeId;
    private final Long reviewId;
    private final String placeName;
    private final Double rating;
    private final HashtagInfoDTO mainTag;
    private final List<HashtagInfoDTO> subTags;
    private final String imageUrl;
    private final String memo;
    private final LocalDateTime createdAt;


    public ReviewDetailsResponseDTO(Review review) {
        Place place = review.getPlace();
        List<PlaceHashtag> placeHashtags = place.getPlaceHashtagList();

        this.placeId = place.getId();
        this.reviewId = review.getId();
        this.placeName = place.getName();
        this.rating = review.getRating();
        this.imageUrl = review.getImageUrl();
        this.memo = review.getMemo();
        this.createdAt = review.getCreatedAt();

        this.mainTag = placeHashtags.stream()
                .filter(PlaceHashtag::getIsMain)
                .map(ph -> new HashtagInfoDTO(ph.getUserHashtag()))
                .findFirst()
                .orElse(null);

        this.subTags = placeHashtags.stream()
                .filter(ph -> !ph.getIsMain())
                .map(ph -> new HashtagInfoDTO(ph.getUserHashtag()))
                .collect(Collectors.toList());
    }
}