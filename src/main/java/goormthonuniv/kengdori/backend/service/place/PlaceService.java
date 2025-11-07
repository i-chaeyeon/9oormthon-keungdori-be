package goormthonuniv.kengdori.backend.service.place;

import goormthonuniv.kengdori.backend.domain.place.PlaceMainTag;
import goormthonuniv.kengdori.backend.domain.place.PlaceMainTagRepository;
import goormthonuniv.kengdori.backend.domain.review.ReviewHashtag;
import goormthonuniv.kengdori.backend.domain.review.ReviewHashtagRepository;
import goormthonuniv.kengdori.backend.domain.user.User;
import goormthonuniv.kengdori.backend.domain.user.UserRepository;
import goormthonuniv.kengdori.backend.dto.place.BoundedPlaceResponseDTO;
import goormthonuniv.kengdori.backend.domain.place.Place;
import goormthonuniv.kengdori.backend.domain.place.PlaceRepository;
import goormthonuniv.kengdori.backend.domain.review.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final PlaceMainTagRepository placeMainTagRepository;
    private final UserRepository userRepository;
    private final ReviewHashtagRepository reviewHashtagRepository;
    private final ReviewRepository reviewRepository;

    public Place findOrCreatePlace(Place place){
        return placeRepository.findByGoogleId(place.getGoogleId())
                .orElseGet(() -> placeRepository.save(place));
    }

    public List<BoundedPlaceResponseDTO> getReviewedPlacesInBoundary(
            Long userId,
            BigDecimal minX, BigDecimal maxX,
            BigDecimal minY, BigDecimal maxY,
            BigDecimal currentX, BigDecimal currentY
    ) {
        List<Place> places = placeRepository.findPlacesWithUserReviewsInBoundary(
                userId, minX, maxX, minY, maxY
        );

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자 없음"));

        return places.stream()
                .map(place -> {
                    double distance = calcDistance(
                            place.getYCoordinate(), place.getXCoordinate(),
                            currentY, currentX
                    );
                    PlaceMainTag mainTag = placeMainTagRepository.findByPlaceAndUser(place, user).orElse(null);
                    log.info("Place: {}, mainTag: {}", place.getName(), mainTag);

                    List<ReviewHashtag> subTags = reviewHashtagRepository.findByPlaceAndUser(place, user);
                    log.info("SubTags 개수: {}", subTags.size());


                    return new BoundedPlaceResponseDTO(place, distance, mainTag, subTags);
                })
                .sorted(Comparator.comparing(BoundedPlaceResponseDTO::getDistance))
                .collect(Collectors.toList());
    }


    private double calcDistance(BigDecimal lat1, BigDecimal lon1, BigDecimal lat2, BigDecimal lon2) {
        double R = 6371000; // 지구 반지름 (m)
        double dLat = Math.toRadians(lat2.doubleValue() - lat1.doubleValue());
        double dLon = Math.toRadians(lon2.doubleValue() - lon1.doubleValue());
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1.doubleValue()))
                * Math.cos(Math.toRadians(lat2.doubleValue()))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // meter 단위
    }
}
