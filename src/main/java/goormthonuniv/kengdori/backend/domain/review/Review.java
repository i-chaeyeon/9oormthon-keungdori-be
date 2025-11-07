package goormthonuniv.kengdori.backend.domain.review;

import goormthonuniv.kengdori.backend.domain.user.User;
import goormthonuniv.kengdori.backend.domain.place.Place;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "review")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    private LocalDateTime createdAt;

    @Column(columnDefinition = "TEXT")
    private String memo;

    @Column(nullable = false)
    private Double rating;

    @Column(columnDefinition = "TEXT")
    private String imageUrl;

    @Builder.Default
    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE)
    private List<ReviewHashtag> hashtags = new ArrayList<>();

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
    }
}