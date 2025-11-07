package goormthonuniv.kengdori.backend.domain.hashtag;

import goormthonuniv.kengdori.backend.domain.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserHashtagRepository extends JpaRepository<UserHashtag, Long> {

    Optional<UserHashtag> findByUserAndHashtag(User user, String hashtag);

    List<UserHashtag> findByUser(User user);

    List<UserHashtag> findByHashtag(String hashtag);

    void deleteByUserAndHashtag(User user, String hashtag);
}
