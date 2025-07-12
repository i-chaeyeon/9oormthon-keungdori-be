package goormthonuniv.kengdori.backend.DTO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UserResponseDTO {
    private final Long id;
    private final String userName;
    private final String kengColor;
    private final String profileImage;
}
