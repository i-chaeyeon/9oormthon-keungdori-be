package goormthonuniv.kengdori.backend.domain.place;

import goormthonuniv.kengdori.backend.domain.user.User;
import goormthonuniv.kengdori.backend.domain.hashtag.UserHashtag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlaceMainTagRepository extends JpaRepository<PlaceMainTag, Long> {

    Optional<PlaceMainTag> findByPlaceAndUser(Place place, User user);

    Optional<PlaceMainTag> findByPlaceAndUserAndUserHashtag(Place place, User user, UserHashtag hashtag);

    @Modifying
    @Query("DELETE FROM PlaceMainTag p WHERE p.place = :place AND p.user = :user")
    void deleteByPlaceAndUser(@Param("place") Place place, @Param("user") User user);
}
