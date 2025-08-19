package goormthonuniv.kengdori.backend.repository;

import goormthonuniv.kengdori.backend.domain.User;
import goormthonuniv.kengdori.backend.domain.UserHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserHashtagRepository extends JpaRepository<UserHashtag, Long> {

    // JPA로 기본적인 CRUD 들어감
    // 조회 메서드만 추가해서 사용하면 됨

    Optional<UserHashtag> findByUserAndHashtag(User user, String hashtag);
}
