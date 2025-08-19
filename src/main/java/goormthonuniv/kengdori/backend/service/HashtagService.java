package goormthonuniv.kengdori.backend.service;

import goormthonuniv.kengdori.backend.DTO.HashtagRequestDTO;
import goormthonuniv.kengdori.backend.DTO.HashtagResponseDTO;
import goormthonuniv.kengdori.backend.domain.User;
import goormthonuniv.kengdori.backend.domain.UserHashtag;
import goormthonuniv.kengdori.backend.repository.UserHashtagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class HashtagService {

    private final UserHashtagRepository userHashtagRepository;

    private static final Map<String, String> COLOR_MAP = Map.of(
            "ADADAD" , "FFFFFF",
            "C5711A", "FFFFFF"
            /// TODO 디자인 보고 추가
    );

    private String backgroundFontMapper(String backgroundColor){
        String fontColor = COLOR_MAP.get(backgroundColor);
        if(fontColor == null){
            throw new IllegalArgumentException("폰트 색상 매핑 오류 :: 배경색 : " + backgroundColor);
        }

        return fontColor;
    }

    private UserHashtag createUserHashtag(User user, String hashtag) {

        final String defaultBackgroundColor = "ADADAD";
        final String defaultFontColor = "FFFFFF";

        UserHashtag newHashtag = UserHashtag.builder()
                .user(user)
                .hashtag(hashtag)
                .backgroundColor(defaultBackgroundColor)
                .fontColor(defaultFontColor)
                .build();
        return userHashtagRepository.save(newHashtag);
    }

    public HashtagResponseDTO findOrCreateUserHashtag(User user, String hashtag) {

        return userHashtagRepository.findByUserAndHashtag(user, hashtag)
                .map(existing -> HashtagResponseDTO.builder()
                        .hashtag(existing.getHashtag())
                        .backgroundColor(existing.getBackgroundColor())
                        .fontColor(existing.getFontColor())
                        .status("exists")
                        .build())
                .orElseGet(() -> {
                    UserHashtag created = createUserHashtag(user, hashtag);
                    return HashtagResponseDTO.builder()
                            .hashtag(created.getHashtag())
                            .backgroundColor(created.getBackgroundColor())
                            .fontColor(created.getFontColor())
                            .status("created")
                            .build();
                });
    }

    public HashtagResponseDTO changeColor(User user, HashtagRequestDTO hashtagRequestDTO){

        UserHashtag userHashtag = userHashtagRepository.findByUserAndHashtag(user, hashtagRequestDTO.hashtag)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 해시태그"));

        userHashtag.setBackgroundColor(hashtagRequestDTO.backgroundColor);
        userHashtag.setFontColor(backgroundFontMapper(hashtagRequestDTO.backgroundColor));
        userHashtagRepository.save(userHashtag);

        return HashtagResponseDTO.builder()
                .hashtag(userHashtag.getHashtag())
                .backgroundColor(userHashtag.getBackgroundColor())
                .fontColor(userHashtag.getFontColor())
                .status("updated")
                .build();
    }

    public UserHashtag findUserHashtag(User user, String hashtag){
        log.info("해시태그 조회 시도 - 사용자: {}, 해시태그: {}", user.getId(), hashtag);

        return userHashtagRepository.findByUserAndHashtag(user, hashtag)
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 해시태그 요청 - 사용자: {}, 해시태그: {}", user.getId(), hashtag);
                    return new RuntimeException("등록되지 않은 해시태그");
                });
    }
}
