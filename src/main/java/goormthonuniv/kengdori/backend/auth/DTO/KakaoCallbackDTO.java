package goormthonuniv.kengdori.backend.auth.DTO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class KakaoCallbackDTO {

    private final Boolean exists;
    private final String accessToken;

}
