package goormthonuniv.kengdori.backend.controller;

import goormthonuniv.kengdori.backend.DTO.ReviewRequestDTO;
import goormthonuniv.kengdori.backend.DTO.ReviewResponseDTO;
import goormthonuniv.kengdori.backend.DTO.ReviewUpdateRequestDTO;
import goormthonuniv.kengdori.backend.DTO.UserResponseDTO;
import goormthonuniv.kengdori.backend.JWT.JwtUtil;
import goormthonuniv.kengdori.backend.domain.User;
import goormthonuniv.kengdori.backend.service.ReviewServiceImpl;
import goormthonuniv.kengdori.backend.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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

    @PatchMapping("/{reviewID}")
    public ResponseEntity<ReviewResponseDTO> editReview(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long reviewId,
            @RequestBody ReviewUpdateRequestDTO reviewUpdateRequestDTO
    ) {
        User user = findUser(authHeader);
        ReviewResponseDTO response = reviewService.updateReview(user, reviewId, reviewUpdateRequestDTO);
        return ResponseEntity.status(200).body(response);
    }

}
