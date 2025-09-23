package goormthonuniv.kengdori.backend.review.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ReviewUpdateRequestDTO {

    private final Double rating;
    private final String mainTag;
    private final List<String> subTags;
    private final String imageUrl;
    private final String memo;
}
