package goormthonuniv.kengdori.backend.DTO;

import goormthonuniv.kengdori.backend.domain.PlaceHashtag;
import goormthonuniv.kengdori.backend.domain.Review;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ReviewListResponseDTO {
    private final Long reviewId;
    private final Double rating;
    private final String memo;
    private final HashtagInfoDTO mainTag;
    private final List<HashtagInfoDTO> subTags;

    public ReviewListResponseDTO(Review review) {
        this.reviewId = review.getId();
        this.rating = review.getRating();
        this.memo = review.getMemo();

        List<PlaceHashtag> placeHashtags = review.getPlace().getPlaceHashtagList();

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