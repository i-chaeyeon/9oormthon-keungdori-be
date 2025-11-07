package goormthonuniv.kengdori.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table
@Builder
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Getter
@Setter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 여러 메모를 하나의 사용자가 작성
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY) // 여러 메모를 하나의 장소에 대해 작성
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    private LocalDateTime createdAt;

    @Column(columnDefinition = "TEXT")
    private String memo;
    private Double rating;

    @Column(columnDefinition = "TEXT")
    private String imageUrl;

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
    }
}