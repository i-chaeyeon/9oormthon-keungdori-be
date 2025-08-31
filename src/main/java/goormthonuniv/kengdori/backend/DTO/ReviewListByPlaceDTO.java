package goormthonuniv.kengdori.backend.DTO;

import goormthonuniv.kengdori.backend.domain.Place;
import goormthonuniv.kengdori.backend.domain.Review;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ReviewListByPlaceDTO {
    private final PlaceInfoDTO placeInfo;
    private final List<ReviewListResponseDTO> reviews;
    private final PageInfoDTO pageInfo;

    public ReviewListByPlaceDTO(Place place, Page<Review> reviewPage) {
        this.placeInfo = new PlaceInfoDTO(place);
        this.reviews = reviewPage.getContent().stream()
                .map(ReviewListResponseDTO::new)
                .collect(Collectors.toList());
        this.pageInfo = new PageInfoDTO(reviewPage);
    }
}