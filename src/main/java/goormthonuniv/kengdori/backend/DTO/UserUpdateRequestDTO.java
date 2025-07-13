package goormthonuniv.kengdori.backend.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserUpdateRequestDTO {
    private String userName;
    private String userId;
    private Boolean search;
    private String kengColor;
    private String profileImage;
}
