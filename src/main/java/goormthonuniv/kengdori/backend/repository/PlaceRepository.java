package goormthonuniv.kengdori.backend.repository;

import goormthonuniv.kengdori.backend.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    // JPA로 기본적인 CRUD 들어감
    // 조회 메서드만 추가해서 사용하면 됨
    // save(User user), findById, findAll, save, deleteById, delete(User user)

    Optional<Place> findBygoogleId(String googleId);
}
