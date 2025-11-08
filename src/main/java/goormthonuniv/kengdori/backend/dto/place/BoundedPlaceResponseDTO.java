package goormthonuniv.kengdori.backend.dto.place;

import goormthonuniv.kengdori.backend.domain.place.Place;
import goormthonuniv.kengdori.backend.domain.place.PlaceMainTag;
import goormthonuniv.kengdori.backend.domain.review.ReviewHashtag;
import goormthonuniv.kengdori.backend.dto.hashtag.HashtagInfoDTO;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BoundedPlaceResponseDTO {

    private final String placeName;
    private final String address;
    private final String googleId;
    private final BigDecimal xCoordinate;
    private final BigDecimal yCoordinate;

    private final HashtagInfoDTO mainTag;
    private final List<HashtagInfoDTO> subTags;
    private final double distance;

    private final String latestImageUrl;

    public BoundedPlaceResponseDTO(
            Place place,
            double distance,
            PlaceMainTag mainTagEntity,
            List<ReviewHashtag> reviewHashtags,
            String latestImageUrl
    ) {
        this.placeName = place.getName();
        this.address = place.getAddress();
        this.googleId = place.getGoogleId();
        this.xCoordinate = place.getXCoordinate();
        this.yCoordinate = place.getYCoordinate();
        this.distance = distance;
        this.latestImageUrl = latestImageUrl;

        this.mainTag = (mainTagEntity != null)
                ? new HashtagInfoDTO(mainTagEntity.getUserHashtag())
                : null;

        this.subTags = reviewHashtags.stream()
                .map(rh -> new HashtagInfoDTO(rh.getUserHashtag()))
                .collect(Collectors.toList());
    }
}
