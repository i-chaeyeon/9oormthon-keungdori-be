package goormthonuniv.kengdori.backend.DTO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Getter
public class UserRequestDTO {

    private final String userName;
    private final String userId;
    private final Boolean search;
    private final String kengColor;
    private final String profileImage;


}
