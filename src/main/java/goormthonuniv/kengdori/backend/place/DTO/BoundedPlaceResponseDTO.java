package goormthonuniv.kengdori.backend.place.DTO;

import goormthonuniv.kengdori.backend.hashtag.DTO.HashtagInfoDTO;
import goormthonuniv.kengdori.backend.domain.PlaceHashtag;
import goormthonuniv.kengdori.backend.domain.Place;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BoundedPlaceResponseDTO {
    private String placeName;
    private String address;
    private String googleId;
    private BigDecimal xCoordinate;
    private BigDecimal yCoordinate;
    private HashtagInfoDTO mainTag;
    private List<HashtagInfoDTO> subTags;
    private double distance;

    public BoundedPlaceResponseDTO(Place place, double distance) {
        this.placeName = place.getName();
        this.address = place.getAddress();
        this.googleId = place.getGoogleId();
        this.xCoordinate = place.getXCoordinate();
        this.yCoordinate = place.getYCoordinate();
        this.mainTag = place.getPlaceHashtagList().stream()
                .filter(PlaceHashtag::getIsMain)
                .map(ph -> new HashtagInfoDTO(ph.getUserHashtag()))
                .findFirst()
                .orElse(null);
        this.subTags = place.getPlaceHashtagList().stream()
                .filter(ph -> !ph.getIsMain())
                .map(ph -> new HashtagInfoDTO(ph.getUserHashtag()))
                .collect(Collectors.toList());
        this.distance = distance;
    }
}
