package goormthonuniv.kengdori.backend.repository;

import goormthonuniv.kengdori.backend.domain.Place;
import goormthonuniv.kengdori.backend.domain.PlaceHashtag;
import goormthonuniv.kengdori.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceHashtagRepository extends JpaRepository<PlaceHashtag, Long> {

    // JPA로 기본적인 CRUD 들어감
    // 조회 메서드만 추가해서 사용하면 됨

    void deleteByPlaceAndUserHashtag_User(Place place, User user);
}
