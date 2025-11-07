package goormthonuniv.kengdori.backend.domain.hashtag;


import goormthonuniv.kengdori.backend.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

// 어떤 사용자가 생성한 해시태그인지 + 해시태그 내용 관련
@Entity
@Table
@Builder
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Getter
@Setter
public class UserHashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 여러 해시태그를 하나의 사용자가 작성
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String hashtag; // 해시태그 내용
    private String backgroundColor; // 지정된 배경 색상
    private String fontColor; // 지정된 폰트 색상
}