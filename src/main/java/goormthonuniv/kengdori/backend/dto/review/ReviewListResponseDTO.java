package goormthonuniv.kengdori.backend.dto.review;

import lombok.Getter;

import java.util.List;

@Getter
public class ReviewListResponseDTO {

    private final Long reviewId;
    private final Double rating;
    private final String memo;
    private final String mainTag;
    private final List<String> subTags;
    private final String imageUrl;

    public ReviewListResponseDTO(Long reviewId, Double rating, String memo,
                                 String mainTag, List<String> subTags, String imageUrl) {

        this.reviewId = reviewId;
        this.rating = rating;
        this.memo = memo;
        this.mainTag = mainTag;
        this.subTags = subTags;
        this.imageUrl = imageUrl;
    }
}
