package goormthonuniv.kengdori.backend.dto.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class ReviewResponseDTO {

    private final Long placeId;
    private final Long reviewId;

    private final String placeName;

    private final Double rating;
    private final String mainTag;
    private final List<String> subTags;
    private final String imageUrl;
    private final String memo;

    private LocalDateTime createdAt;
}
