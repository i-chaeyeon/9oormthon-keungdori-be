package goormthonuniv.kengdori.backend.dto.place;

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
