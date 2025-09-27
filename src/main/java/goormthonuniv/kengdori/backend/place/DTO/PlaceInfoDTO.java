package goormthonuniv.kengdori.backend.place.DTO;

import goormthonuniv.kengdori.backend.place.domain.Place;
import lombok.Getter;

@Getter
public class PlaceInfoDTO {
    private final Long placeId;
    private final String placeName;
    private final String address;

    public PlaceInfoDTO(Place place) {
        this.placeId = place.getId();
        this.placeName = place.getName();
        this.address = place.getAddress();
    }
}