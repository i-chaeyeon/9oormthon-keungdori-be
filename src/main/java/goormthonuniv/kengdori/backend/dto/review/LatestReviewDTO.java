package goormthonuniv.kengdori.backend.dto.review;

import goormthonuniv.kengdori.backend.domain.review.Review;
import lombok.Getter;

@Getter
public class LatestReviewDTO {
    private final Long reviewId;
    private final String imageUrl;
    private final String memo;
    private final Double rating;
    private final String userColor;

    public LatestReviewDTO(Review review) {
        this.reviewId = review.getId();
        this.imageUrl = review.getImageUrl();
        this.memo = review.getMemo();
        this.rating = review.getRating();
        this.userColor = review.getUser().getKengColor();
    }
}
