package goormthonuniv.kengdori.backend.repository;

import goormthonuniv.kengdori.backend.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // JPA로 기본적인 CRUD 들어감
    // 조회 메서드만 추가해서 사용하면 됨
    // save(User user), findById, findAll, save, deleteById, delete(User user)

}
