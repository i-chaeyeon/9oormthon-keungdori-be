package goormthonuniv.kengdori.backend.DTO;

import goormthonuniv.kengdori.backend.domain.User;
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

    public  User toUser(Long kakaoId){
        return User.builder()
                .userName(userName)
                .userId(userId)
                .search(search)
                .kengColor(kengColor)
                .profileImage(profileImage)
                .subscription(false)
                .kakaoId(kakaoId)
                .build();

    }
}
