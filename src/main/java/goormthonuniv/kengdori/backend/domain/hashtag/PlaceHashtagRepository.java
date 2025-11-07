package goormthonuniv.kengdori.backend.domain.hashtag;

import goormthonuniv.kengdori.backend.domain.place.Place;
import goormthonuniv.kengdori.backend.domain.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceHashtagRepository extends JpaRepository<PlaceHashtag, Long> {

    // JPA로 기본적인 CRUD 들어감
    // 조회 메서드만 추가해서 사용하면 됨

    List<PlaceHashtag> findByPlaceAndUser(Place place, User user);

    void deleteByPlaceAndUser(Place place, User user);
}
