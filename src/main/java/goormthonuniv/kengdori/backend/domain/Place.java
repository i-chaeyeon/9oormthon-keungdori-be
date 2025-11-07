package goormthonuniv.kengdori.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    private String address; // 장소 주소
    private String googleId; // 구글 API에서 장소의 id

    @Column(precision = 10, scale = 6)
    private BigDecimal xCoordinate; // x 좌표
    @Column(precision = 10, scale = 6)
    private BigDecimal yCoordinate; // y 좌표


    // Place 삭제 시 Review와 Hashtag 전부 삭제 (cascade)
    @Builder.Default
    @OneToMany(mappedBy = "place", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Review> reviewList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "place", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PlaceHashtag> placeHashtagList = new ArrayList<>();
}