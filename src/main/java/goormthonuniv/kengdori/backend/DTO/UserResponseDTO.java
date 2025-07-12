package goormthonuniv.kengdori.backend.DTO;

import goormthonuniv.kengdori.backend.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UserResponseDTO {
    private final Long id;
    private final String userName;
    private final String userId;
    private final Boolean search;
    private final String kengColor;
    private final String profileImage;

    public static UserResponseDTO from(User user){
        return new UserResponseDTO(
                user.getId(),
                user.getUserName(),
                user.getUserId(),
                user.isSearch(),
                user.getKengColor(),
                user.getProfileImage()
        );
    }
}
