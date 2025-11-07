package goormthonuniv.kengdori.backend.place.repository;

import goormthonuniv.kengdori.backend.domain.Place;
import goormthonuniv.kengdori.backend.domain.User;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    // JPA로 기본적인 CRUD 들어감
    // 조회 메서드만 추가해서 사용하면 됨
    // save(User user), findById, findAll, save, deleteById, delete(User user)

    Optional<Place> findBygoogleId(String googleId);

    @Query("SELECT DISTINCT p FROM Place p JOIN p.reviewList r WHERE r.user = :user AND p.name LIKE %:keyword%")
    Page<Place> findPlacesByUserReviewAndKeyword(
            @Param("user") User user,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Query("SELECT p FROM Place p JOIN p.placeHashtagList ph JOIN ph.userHashtag uh WHERE uh.hashtag = :hashtag")
    Page<Place> findByHashtag(
            @Param("hashtag") String hashtag,
            Pageable pageable
    );

    @Query("""
    SELECT DISTINCT p
    FROM Review r
    JOIN r.place p
    JOIN FETCH p.placeHashtagList ph
    JOIN FETCH ph.userHashtag uh
    WHERE r.user.id = :userId
      AND p.xCoordinate BETWEEN :minX AND :maxX
      AND p.yCoordinate BETWEEN :minY AND :maxY
    """)
    List<Place> findPlacesWithUserReviewsInBoundary(
            @Param("userId") Long userId,
            @Param("minX") BigDecimal minX,
            @Param("maxX") BigDecimal maxX,
            @Param("minY") BigDecimal minY,
            @Param("maxY") BigDecimal maxY
    );

}
