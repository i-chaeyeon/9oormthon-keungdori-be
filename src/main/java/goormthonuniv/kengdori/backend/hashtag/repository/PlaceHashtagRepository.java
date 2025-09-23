package goormthonuniv.kengdori.backend.hashtag.repository;

import goormthonuniv.kengdori.backend.place.domain.Place;
import goormthonuniv.kengdori.backend.hashtag.domain.PlaceHashtag;
import goormthonuniv.kengdori.backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceHashtagRepository extends JpaRepository<PlaceHashtag, Long> {

    // JPA로 기본적인 CRUD 들어감
    // 조회 메서드만 추가해서 사용하면 됨

    void deleteByPlaceAndUserHashtag_User(Place place, User user);
}
