package goormthonuniv.kengdori.backend.controller;

import goormthonuniv.kengdori.backend.DTO.*;
import goormthonuniv.kengdori.backend.JWT.JwtUtil;
import goormthonuniv.kengdori.backend.domain.User;
import goormthonuniv.kengdori.backend.exception.NoResultsFoundException;
import goormthonuniv.kengdori.backend.service.ReviewServiceImpl;
import goormthonuniv.kengdori.backend.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/reviews")
public class ReviewController {

    private final JwtUtil jwtUtil;
    private final UserServiceImpl userService;
    private final ReviewServiceImpl reviewService;

    private User findUser(String authHeader){
        String accessToken = authHeader.replace("Bearer ", "");
        Long kakaoId = jwtUtil.getClaimsToken(accessToken).get("kakaoId", Long.class);
        return userService.findUserByKakaoId(kakaoId);
    }

    @PostMapping("")
    public ResponseEntity<ReviewResponseDTO> postNewReview(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody ReviewRequestDTO reviewRequestDTO
    ) {
        User user = findUser(authHeader);
        ReviewResponseDTO response = reviewService.createReview(user, reviewRequestDTO);
        return ResponseEntity.status(201).body(response);
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDTO> editReview(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long reviewId,
            @RequestBody ReviewUpdateRequestDTO reviewUpdateRequestDTO
    ) {
        User user = findUser(authHeader);
        ReviewResponseDTO response = reviewService.updateReview(user, reviewId, reviewUpdateRequestDTO);
        return ResponseEntity.status(200).body(response);
    }


    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long reviewId
    ) {
        User user = findUser(authHeader);
        reviewService.deleteReview(user, reviewId);
        return ResponseEntity.noContent().build();
    }

    ///  [3-1]
    @GetMapping("/visited")
    public ResponseEntity<ReadResponseDTO<VisitedPlaceResponseDTO>> searchMyVisitedPlaces(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("search") String keyword,
            @RequestParam(defaultValue = "0") int page) {

        User user = findUser(authHeader);
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());

        Page<VisitedPlaceResponseDTO> resultPage = reviewService.searchMyReviewedPlaces(user, keyword, pageable);

        if (resultPage.isEmpty()) {
            throw new NoResultsFoundException("검색 결과가 없습니다.");
        }

        ReadResponseDTO<VisitedPlaceResponseDTO> response = new ReadResponseDTO<>(resultPage);
        return ResponseEntity.ok(response);
    }

    ///  [3-2]
    @GetMapping("/hashtag")
    public ResponseEntity<ReadResponseDTO<VisitedPlaceResponseDTO>> findPlacesByHashtag(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("tag") String hashtag,
            @RequestParam(defaultValue = "0") int page) {

        if (!hashtag.startsWith("#")) {
            hashtag = "#" + hashtag;
        }

        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());

        Page<VisitedPlaceResponseDTO> resultPage = reviewService.findPlacesByHashtag(hashtag, pageable);

        if (resultPage.isEmpty()) {
            throw new NoResultsFoundException("검색 결과가 없습니다.");
        }

        ReadResponseDTO<VisitedPlaceResponseDTO> response = new ReadResponseDTO<>(resultPage);
        return ResponseEntity.ok(response);
    }

    ///  [3-3]
    @GetMapping("/place/{googleId}")
    public ResponseEntity<ReviewListByPlaceDTO> findMyReviewsByPlace(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String googleId,
            @RequestParam(defaultValue = "0") int page
    ){
        User user = findUser(authHeader);
        Pageable pageable = PageRequest.of(page, 10, Sort.by("createdAt").descending());

        ReviewListByPlaceDTO result = reviewService.findMyReviewsByPlace(googleId, user, pageable);

        return ResponseEntity.ok(result);
    }
}
