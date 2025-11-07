package goormthonuniv.kengdori.backend.dto.place;

import goormthonuniv.kengdori.backend.domain.place.Place;
import goormthonuniv.kengdori.backend.domain.place.PlaceMainTag;
import goormthonuniv.kengdori.backend.domain.review.ReviewHashtag;
import goormthonuniv.kengdori.backend.domain.user.User;
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

    private final HashtagInfoDTO mainTag;    // 유저가 지정한 메인 태그
    private final List<HashtagInfoDTO> subTags; // 리뷰에서 사용된 태그들
    private final double distance;

    public BoundedPlaceResponseDTO(Place place,
                                   double distance,
                                   PlaceMainTag mainTagEntity,
                                   List<ReviewHashtag> reviewHashtags) {
        this.placeName = place.getName();
        this.address = place.getAddress();
        this.googleId = place.getGoogleId();
        this.xCoordinate = place.getXCoordinate();
        this.yCoordinate = place.getYCoordinate();
        this.distance = distance;

        this.mainTag = (mainTagEntity != null)
                ? new HashtagInfoDTO(mainTagEntity.getUserHashtag())
                : null;

        this.subTags = reviewHashtags.stream()
                .map(rh -> new HashtagInfoDTO(rh.getUserHashtag()))
                .collect(Collectors.toList());
    }
}
