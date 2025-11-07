package goormthonuniv.kengdori.backend.domain.place;

import goormthonuniv.kengdori.backend.domain.user.User;
import goormthonuniv.kengdori.backend.domain.hashtag.UserHashtag;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "place_main_tag",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_place_user",
                        columnNames = {"place_id", "user_id"}
                )
        }
)
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor @Builder
public class PlaceMainTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_hashtag_id", nullable = false)
    private UserHashtag userHashtag;
}
