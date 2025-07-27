package goormthonuniv.kengdori.backend.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Builder
@AllArgsConstructor
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@Getter
@Setter
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // 장소명
    private String kakaoId; // 카카오 API에서 장소의 id
    private String xCoordinate; // x 좌표
    private String yCoordinate; // y 좌표
}