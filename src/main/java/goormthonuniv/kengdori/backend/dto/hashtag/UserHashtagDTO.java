package goormthonuniv.kengdori.backend.dto.hashtag;

import goormthonuniv.kengdori.backend.domain.hashtag.UserHashtag;
import lombok.Getter;

@Getter
public class UserHashtagDTO {
    private final Long id;
    private final String hashtag;
    private final String backgroundColor;
    private final String fontColor;

    public UserHashtagDTO(UserHashtag userHashtag) {
        this.id = userHashtag.getId();
        this.hashtag = userHashtag.getHashtag();
        this.backgroundColor = userHashtag.getBackgroundColor();
        this.fontColor = userHashtag.getFontColor();
    }
}
