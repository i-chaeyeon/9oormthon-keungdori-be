package goormthonuniv.kengdori.backend.domain.place;

import goormthonuniv.kengdori.backend.domain.review.Review;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "place",
        indexes = {
                @Index(name = "idx_place_google_id", columnList = "googleId"),
                @Index(name = "idx_place_xy", columnList = "x_coordinate,y_coordinate")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_place_google_id", columnNames = {"googleId"})
        }
)
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor @Builder
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;

    @Column(nullable = false)
    private String googleId;

    @Column(name = "x_coordinate", precision = 10, scale = 6, nullable = false)
    private BigDecimal xCoordinate;

    @Column(name = "y_coordinate", precision = 10, scale = 6, nullable = false)
    private BigDecimal yCoordinate;

    // Place 삭제 시 관련 리뷰 전부 삭제
    @OneToMany(mappedBy = "place", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();
}
