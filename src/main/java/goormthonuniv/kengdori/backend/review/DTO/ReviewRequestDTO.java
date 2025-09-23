package goormthonuniv.kengdori.backend.review.DTO;

import goormthonuniv.kengdori.backend.place.domain.Place;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class ReviewRequestDTO {

    private final String name;
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
                .name(this.name)
                .address(this.address)
                .googleId(this.googleId)
                .xCoordinate(this.xCoordinate)
                .yCoordinate(this.yCoordinate)
                .build();
    }
}