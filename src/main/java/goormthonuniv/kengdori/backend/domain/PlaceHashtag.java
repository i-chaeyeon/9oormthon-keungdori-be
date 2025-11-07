package goormthonuniv.kengdori.backend.domain;

import jakarta.persistence.*;
import lombok.*;

// 어떤 장소에 어떤 해시태그가 달려있는지
@Entity
@Table
@Builder
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Getter
@Setter
public class PlaceHashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_hashtag_id", nullable = false)
    private UserHashtag userHashtag;

    private Boolean isMain; // 대표 해시태그 여부
}