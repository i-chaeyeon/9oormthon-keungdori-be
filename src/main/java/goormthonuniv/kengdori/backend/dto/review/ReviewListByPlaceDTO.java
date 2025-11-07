package goormthonuniv.kengdori.backend.dto.review;

import goormthonuniv.kengdori.backend.dto.place.PlaceInfoDTO;
import goormthonuniv.kengdori.backend.domain.place.Place;
import goormthonuniv.kengdori.backend.global.DTO.PageInfoDTO;
import lombok.Getter;

import java.util.List;

@Getter
public class ReviewListByPlaceDTO {
    private final PlaceInfoDTO placeInfo;
    private final List<ReviewListResponseDTO> reviews;
    private final PageInfoDTO pageInfo;

    public ReviewListByPlaceDTO(Place place, List<ReviewListResponseDTO> reviews, PageInfoDTO pageInfo) {
        this.placeInfo = (place != null) ? new PlaceInfoDTO(place) : null;
        this.reviews = reviews;
        this.pageInfo = pageInfo;
    }
}
