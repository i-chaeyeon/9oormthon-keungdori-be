package goormthonuniv.kengdori.backend.service.hashtag;

import goormthonuniv.kengdori.backend.domain.hashtag.UserHashtag;
import goormthonuniv.kengdori.backend.domain.hashtag.UserHashtagRepository;
import goormthonuniv.kengdori.backend.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserHashtagService {

    private UserHashtagRepository userHashtagRepository;

    public UserHashtag findOrCreate(User user, String hashtag) {
        if (hashtag == null || hashtag.isBlank()) {
            throw new IllegalArgumentException("해시태그 값이 비어있습니다.");
        }

        String normalized = normalize(hashtag);

        return userHashtagRepository.findByUserAndHashtag(user, normalized)
                .orElseGet(() -> createUserHashtag(user, normalized));
    }

    public UserHashtag find(User user, String hashtag) {
        String normalized = normalize(hashtag);

        return userHashtagRepository.findByUserAndHashtag(user, normalized)
                .orElseThrow(() -> new IllegalArgumentException("해당 해시태그를 보유하고 있지 않습니다."));
    }

    private String normalize(String hashtag) {
        return hashtag.startsWith("#") ? hashtag.substring(1) : hashtag;
    }

    private UserHashtag createUserHashtag(User user, String hashtag) {
        return userHashtagRepository.save(
                UserHashtag.builder()
                        .user(user)
                        .hashtag(hashtag)
                        .backgroundColor("#adadad") // 기본 색상
                        .fontColor("#ffffff")
                        .build()
        );
    }
}
