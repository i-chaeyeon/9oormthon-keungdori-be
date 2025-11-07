package goormthonuniv.kengdori.backend.user.DTO;

import goormthonuniv.kengdori.backend.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UserResponseDTO {
    private final String accessToken;
    private final Long id;
    private final String userName;
    private final String searchId;
    private final Boolean search;
    private final String kengColor;
    private final String profileImage;

    public static UserResponseDTO from(User user, String accessToken){
        return new UserResponseDTO(
                accessToken,
                user.getId(),
                user.getUserName(),
                user.getSearchId(),
                user.isSearch(),
                user.getKengColor(),
                user.getProfileImage()
        );
    }
}
