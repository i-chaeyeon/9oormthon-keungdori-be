package goormthonuniv.kengdori.backend.dto.place;

import goormthonuniv.kengdori.backend.domain.place.Place;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class VisitedPlaceResponseDTO {

    private final Long placeId;
    private final String placeName;
    private final String address;
    private final String googleId;
    private final BigDecimal xCoordinate;
    private final BigDecimal yCoordinate;

    public VisitedPlaceResponseDTO(Place place){
        this.placeId = place.getId();
        this.placeName = place.getName();
        this.address = place.getAddress();
        this.googleId = place.getGoogleId();
        this.xCoordinate = place.getXCoordinate();
        this.yCoordinate = place.getYCoordinate();
    }
}
