package goormthonuniv.kengdori.backend.review.DTO;

import goormthonuniv.kengdori.backend.place.DTO.PlaceInfoDTO;
import goormthonuniv.kengdori.backend.domain.Place;
import goormthonuniv.kengdori.backend.domain.Review;
import goormthonuniv.kengdori.backend.global.DTO.PageInfoDTO;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.Collections;
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

    public ReviewListByPlaceDTO(){
        this.placeInfo = new PlaceInfoDTO();
        this.reviews = Collections.emptyList();
        this.pageInfo = new PageInfoDTO(Page.empty());
    }
}