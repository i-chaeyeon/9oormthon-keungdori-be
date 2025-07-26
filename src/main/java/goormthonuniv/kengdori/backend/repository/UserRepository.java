package goormthonuniv.kengdori.backend.repository;

import goormthonuniv.kengdori.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // JPA로 기본적인 CRUD 들어감
    // 조회 메서드만 추가해서 사용하면 됨
    // save(User user), findById, findAll, save, deleteById, delete(User user)

    Optional<User> findByKakaoId(Long kakaoId); // 이미 가입한 회원인지 확인시 사용 & 리프레시 토큰 매핑 시 사용
    Optional<User> findByUserId(String userId); // 친구 연결 시 사용자 검색에 사용
    boolean existsBySearchId(String userId); // 아이디 중복 검사
}
