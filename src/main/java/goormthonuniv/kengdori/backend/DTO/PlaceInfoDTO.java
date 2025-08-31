package goormthonuniv.kengdori.backend.DTO;

import goormthonuniv.kengdori.backend.domain.Place;
import lombok.Getter;

@Getter
public class PlaceInfoDTO {
    private final Long placeId;
    private final String name;
    private final String address;

    public PlaceInfoDTO(Place place) {
        this.placeId = place.getId();
        this.name = place.getName();
        this.address = place.getAddress();
    }
}