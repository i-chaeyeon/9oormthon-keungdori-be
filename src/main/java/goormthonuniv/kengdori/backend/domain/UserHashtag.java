package goormthonuniv.kengdori.backend.domain;


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
    private String color; // 지정된 색상
    private Boolean isDefault; // 색상을 지정하지 않을 경우 필요한 필드 -> 근데 필요할까? (회의 TODO)
}