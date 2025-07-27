package goormthonuniv.kengdori.backend.service;

import goormthonuniv.kengdori.backend.DTO.HashtagResponseDTO;
import goormthonuniv.kengdori.backend.domain.User;
import goormthonuniv.kengdori.backend.domain.UserHashtag;
import goormthonuniv.kengdori.backend.repository.UserHashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
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
//        UserHashtag userHashtag = userHashtagRepository.findByUserAndHashtag(user, hashtag)
//                .orElseGet(() -> createUserHashtag(user, hashtag));
//
//        String status = userHashtag.getId() == null ? "created" : "exists";
//
//        return HashtagResponseDTO.builder()
//                .hashtag(userHashtag.getHashtag())
//                .color(userHashtag.getColor())
//                .status(status)
//                .build();
    }


}
