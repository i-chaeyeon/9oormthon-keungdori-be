package goormthonuniv.kengdori.backend.domain.review;

import goormthonuniv.kengdori.backend.domain.user.User;
import goormthonuniv.kengdori.backend.domain.hashtag.UserHashtag;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewHashtagRepository extends JpaRepository<ReviewHashtag, Long> {

    List<ReviewHashtag> findByReviewId(Long reviewId);

    List<ReviewHashtag> findByReviewAndUser(Review review, User user);

    Optional<ReviewHashtag> findByReviewIdAndUserHashtag(Long reviewId, UserHashtag hashtag);

    void deleteByReviewAndUser(Review review, User user);
}
