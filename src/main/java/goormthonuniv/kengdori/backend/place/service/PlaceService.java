package goormthonuniv.kengdori.backend.place.service;

import goormthonuniv.kengdori.backend.place.domain.Place;
import goormthonuniv.kengdori.backend.place.repository.PlaceRepository;
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
