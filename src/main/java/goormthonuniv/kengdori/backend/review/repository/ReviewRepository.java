package goormthonuniv.kengdori.backend.review.repository;

import goormthonuniv.kengdori.backend.domain.Place;
import goormthonuniv.kengdori.backend.domain.Review;
import goormthonuniv.kengdori.backend.domain.User;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // JPA로 기본적인 CRUD 들어감
    // 조회 메서드만 추가해서 사용하면 됨
    // save(User user), findById, findAll, save, deleteById, delete(User user)
    Optional<Review> findById(Long id);

    Page<Review> findByPlaceAndUser(Place place, User user, Pageable pageable);

    Optional<Review> findTopByPlaceOrderByCreatedAtDesc(Place place);

    @Query("""
    SELECT r
    FROM Review r
    JOIN FETCH r.place p
    WHERE p.xCoordinate BETWEEN :minX AND :maxX
      AND p.yCoordinate BETWEEN :minY AND :maxY
    """)
    List<Review> findAllByBoundary(
            @Param("minX") BigDecimal minX,
            @Param("maxX") BigDecimal maxX,
            @Param("minY") BigDecimal minY,
            @Param("maxY") BigDecimal maxY
    );

    boolean existsByUserIdAndCreatedAtAfter(Long userId, LocalDateTime dateTime);
}
