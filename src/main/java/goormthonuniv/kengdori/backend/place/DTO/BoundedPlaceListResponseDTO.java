package goormthonuniv.kengdori.backend.place.DTO;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BoundedPlaceListResponseDTO {
    private String userColor;
    private List<BoundedPlaceResponseDTO> places;
}
