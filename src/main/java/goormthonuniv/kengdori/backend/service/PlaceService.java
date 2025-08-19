package goormthonuniv.kengdori.backend.service;

import goormthonuniv.kengdori.backend.domain.Place;
import goormthonuniv.kengdori.backend.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PlaceService {

    private final PlaceRepository placeRepository;

    public Place findOrCreatePlace(Place place){
        return placeRepository.findBygoogleId(place.getGoogleId())
                .orElseGet(() -> placeRepository.save(place));
    }
}
