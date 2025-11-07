package goormthonuniv.kengdori.backend.hashtag.service;

import goormthonuniv.kengdori.backend.hashtag.DTO.HashtagRequestDTO;
import goormthonuniv.kengdori.backend.hashtag.DTO.HashtagResponseDTO;
import goormthonuniv.kengdori.backend.domain.User;
import goormthonuniv.kengdori.backend.domain.UserHashtag;
import goormthonuniv.kengdori.backend.hashtag.repository.UserHashtagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class HashtagService {

    private final UserHashtagRepository userHashtagRepository;

    private static final Map<String, String> COLOR_MAP = Map.ofEntries(
            Map.entry("#f44259", "#ffffff"),
            Map.entry("#ff83a8", "#ffffff"),
            Map.entry("#ffa6a7", "#ffffff"),
            Map.entry("#ffe0e0", "#f44259"), // 컬러칩 1번째 줄
            Map.entry("#ff5e2d", "#ffffff"),
            Map.entry("#ff953e", "#ffffff"),
            Map.entry("#ffc639", "#ffffff"),
            Map.entry("#fff272", "#ff5e2d"), // 컬러칩 2번째 줄
            Map.entry("#35a23a" , "#ffffff"),
            Map.entry("#88dd00" , "#14479e"),
            Map.entry("#8dd7b0" , "#14479e"),
            Map.entry("#c0f5d6" , "#35a23a"), // 컬러칩 3번째 줄
            Map.entry("#14479e" , "#ffffff"),
            Map.entry("#b83aff" , "#ffffff"),
            Map.entry("#42aedc" , "#ffffff"),
            Map.entry("#b4d8ed" , "#14479e"), // 컬러칩 4번째 줄
            Map.entry("#000000" , "#ffffff"),
            Map.entry("#6a3906" , "#ffffff"),
            Map.entry("#c5711a" , "#ffffff"),
            Map.entry("#adadad" , "#ffffff"), // 컬러칩 5번째 줄 - 이하 디버깅 용 임시 맵
            Map.entry("#b71c1c", "#ffffff"),
            Map.entry("#880e4f", "#ffffff")
    );

    private String backgroundFontMapper(String backgroundColor){
        String fontColor = COLOR_MAP.get(backgroundColor);
        if(fontColor == null){
            log.error("유효하지 않은 배경색 요청: {}", backgroundColor);
            throw new IllegalArgumentException("폰트 색상 매핑 오류 :: 배경색 : " + backgroundColor);
        }

        return fontColor;
    }

    private UserHashtag createUserHashtag(User user, String hashtag) {

        log.info("신규 해시태그 생성 - userId: {}, hashtag: '{}'", user.getId(), hashtag);

        if (hashtag.startsWith("#")) {
            hashtag = hashtag.substring(1);
        }

        final String defaultBackgroundColor = "#adadad";
        final String defaultFontColor = "#ffffff";

        UserHashtag newHashtag = UserHashtag.builder()
                .user(user)
                .hashtag(hashtag)
                .backgroundColor(defaultBackgroundColor)
                .fontColor(defaultFontColor)
                .build();
        return userHashtagRepository.save(newHashtag);
    }

    public HashtagResponseDTO findOrCreateUserHashtag(User user, String hashtag) {

        log.info("해시태그 조회/생성 시도 - userId: {}, hashtag: '{}'", user.getId(), hashtag);

        if (hashtag.startsWith("#")) {
            hashtag = hashtag.substring(1);
        }

        String finalHashtag = hashtag;
        return userHashtagRepository.findByUserAndHashtag(user, hashtag)
                .map(existing -> HashtagResponseDTO.builder()
                        .hashtag(existing.getHashtag())
                        .backgroundColor(existing.getBackgroundColor())
                        .fontColor(existing.getFontColor())
                        .status("exists")
                        .build())
                .orElseGet(() -> {
                    UserHashtag created = createUserHashtag(user, finalHashtag);
                    log.info("created default color : {} ", created.getBackgroundColor());
                    return HashtagResponseDTO.builder()
                            .hashtag(created.getHashtag())
                            .backgroundColor(created.getBackgroundColor())
                            .fontColor(created.getFontColor())
                            .status("created")
                            .build();
                });
    }

    public HashtagResponseDTO changeColor(User user, HashtagRequestDTO hashtagRequestDTO){

        if (!hashtagRequestDTO.backgroundColor.startsWith("#")) {
            hashtagRequestDTO.backgroundColor = "#" + hashtagRequestDTO.backgroundColor;
        }

        if (hashtagRequestDTO.hashtag.startsWith("#")) {
            hashtagRequestDTO.hashtag = hashtagRequestDTO.hashtag.substring(1);
        }

        log.info("해시태그 색상 변경 시도 - userId: {}, hashtag: '{}', color: {}",
                user.getUserName(), hashtagRequestDTO.hashtag, hashtagRequestDTO.backgroundColor);

        UserHashtag userHashtag = userHashtagRepository.findByUserAndHashtag(user, hashtagRequestDTO.hashtag)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 해시태그"));

        userHashtag.setBackgroundColor(hashtagRequestDTO.backgroundColor);
        userHashtag.setFontColor(backgroundFontMapper(hashtagRequestDTO.backgroundColor));
        userHashtagRepository.save(userHashtag);

        log.info("해시태그 색상 변경 완료 - userHashtagId: {} color: {}", userHashtag.getId(), userHashtag.getBackgroundColor());

        return HashtagResponseDTO.builder()
                .hashtag(userHashtag.getHashtag())
                .backgroundColor(userHashtag.getBackgroundColor())
                .fontColor(userHashtag.getFontColor())
                .status("updated")
                .build();
    }

    public UserHashtag findUserHashtag(User user, String hashtag){
        log.info("해시태그 조회 시도 - 사용자: {}, 해시태그: {}", user.getUserName(), hashtag);

        if (hashtag.startsWith("#")) {
            hashtag = hashtag.substring(1);
        }

        String finalHashtag = hashtag;
        return userHashtagRepository.findByUserAndHashtag(user, hashtag)
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 해시태그 요청 - 사용자: {}, 해시태그: {}", user.getId(), finalHashtag);
                    return new RuntimeException("등록되지 않은 해시태그");
                });
    }
}
