package goormthonuniv.kengdori.backend.domain.place;

import goormthonuniv.kengdori.backend.domain.user.User;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    Optional<Place> findByGoogleId(String googleId);

    @Query("""
        SELECT DISTINCT p 
        FROM Place p 
        JOIN p.reviews r 
        WHERE r.user = :user 
        AND p.name LIKE %:keyword%
    """)
    Page<Place> findPlacesByUserReviewAndKeyword(
            @Param("user") User user,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Query("SELECT DISTINCT p FROM Place p " +
            "JOIN Review r ON r.place = p " +
            "JOIN ReviewHashtag rh ON rh.review = r " +
            "JOIN UserHashtag uh ON uh = rh.userHashtag " +
            "WHERE uh.hashtag = :hashtag")
    Page<Place> findPlacesByHashtag(@Param("hashtag") String hashtag, Pageable pageable);

    @Query("""
        SELECT DISTINCT p
        FROM Review r
        JOIN r.place p
        LEFT JOIN r.hashtags rh
        LEFT JOIN rh.userHashtag uh
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

