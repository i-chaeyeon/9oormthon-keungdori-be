package goormthonuniv.kengdori.backend.user.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long kakaoId; // 카카오 로그인 시 유저 정보
    private String userName; // 보여지는 이름

    @Column(unique = true)
    private String searchId; // 검색 시 사용할 아이디 (중복 불가)

    private boolean search; // 검색 허용/거부
    private String kengColor; // 친구에게 보여지는 마커 색상
    private boolean subscription; // 구독 여부

    @Column(columnDefinition = "TEXT")
    private String profileImage; // 본인 프로필 사진
    private LocalDateTime createdAt;

    private String refreshToken;

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
    }
}
