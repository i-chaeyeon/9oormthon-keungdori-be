package goormthonuniv.kengdori.backend.service;

import goormthonuniv.kengdori.backend.DTO.HashtagRequestDTO;
import goormthonuniv.kengdori.backend.DTO.HashtagResponseDTO;
import goormthonuniv.kengdori.backend.domain.User;
import goormthonuniv.kengdori.backend.domain.UserHashtag;
import goormthonuniv.kengdori.backend.repository.UserHashtagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class HashtagService {

    private final UserHashtagRepository userHashtagRepository;

    private UserHashtag createUserHashtag(User user, String hashtag) {

        final String defaultColor = "FFFFFF";

        UserHashtag newHashtag = UserHashtag.builder()
                .user(user)
                .hashtag(hashtag)
                .color(defaultColor)
                .build();
        return userHashtagRepository.save(newHashtag);
    }

    public HashtagResponseDTO findOrCreateUserHashtag(User user, String hashtag) {

        return userHashtagRepository.findByUserAndHashtag(user, hashtag)
                .map(existing -> HashtagResponseDTO.builder()
                        .hashtag(existing.getHashtag())
                        .color(existing.getColor())
                        .status("exists")
                        .build())
                .orElseGet(() -> {
                    UserHashtag created = createUserHashtag(user, hashtag);
                    return HashtagResponseDTO.builder()
                            .hashtag(created.getHashtag())
                            .color(created.getColor())
                            .status("created")
                            .build();
                });
    }

    public HashtagResponseDTO changeColor(User user, HashtagRequestDTO hashtagRequestDTO){

        UserHashtag userHashtag = userHashtagRepository.findByUserAndHashtag(user, hashtagRequestDTO.hashtag)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 해시태그"));

        userHashtag.setColor(hashtagRequestDTO.color);
        userHashtagRepository.save(userHashtag);

        return HashtagResponseDTO.builder()
                .hashtag(userHashtag.getHashtag())
                .color(userHashtag.getColor())
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
