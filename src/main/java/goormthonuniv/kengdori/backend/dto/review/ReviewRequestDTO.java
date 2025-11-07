package goormthonuniv.kengdori.backend.dto.review;

import goormthonuniv.kengdori.backend.domain.place.Place;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class ReviewRequestDTO {

    private final String placeName;
    private final String address;
    private final String googleId;
    private final BigDecimal xCoordinate;
    private final BigDecimal yCoordinate;

    private final Double rating;
    private final String mainTag;
    private final List<String> subTags;
    private final String imageUrl;
    private final String memo;

    public Place toPlace(){
        return Place.builder()
                .name(this.placeName)
                .address(this.address)
                .googleId(this.googleId)
                .xCoordinate(this.xCoordinate)
                .yCoordinate(this.yCoordinate)
                .build();
    }
}