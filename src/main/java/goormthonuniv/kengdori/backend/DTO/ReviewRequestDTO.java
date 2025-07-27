package goormthonuniv.kengdori.backend.DTO;

import goormthonuniv.kengdori.backend.domain.Place;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class ReviewRequestDTO {

    private final String name;
    private final String placeKakaoId;
    private final String xCoordinate;
    private final String yCoordinate;

    private final Double rating;
    private final String mainTag;
    private final List<String> subTags;
    private final String imageUrl;
    private final String memo;

    public Place toPlace(){
        return Place.builder()
                .name(this.name)
                .kakaoId(this.placeKakaoId)
                .xCoordinate(this.xCoordinate)
                .yCoordinate(this.yCoordinate)
                .build();
    }
}