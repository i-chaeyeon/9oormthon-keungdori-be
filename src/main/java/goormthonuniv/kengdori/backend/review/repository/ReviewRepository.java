package goormthonuniv.kengdori.backend.review.repository;

import goormthonuniv.kengdori.backend.place.domain.Place;
import goormthonuniv.kengdori.backend.review.domain.Review;
import goormthonuniv.kengdori.backend.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // JPA로 기본적인 CRUD 들어감
    // 조회 메서드만 추가해서 사용하면 됨
    // save(User user), findById, findAll, save, deleteById, delete(User user)
    Optional<Review> findById(Long id);

    Page<Review> findByPlaceAndUser(Place place, User user, Pageable pageable);
}
